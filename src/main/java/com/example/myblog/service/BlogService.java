package com.example.myblog.service;

import com.alibaba.fastjson.JSON;
import com.example.myblog.bean.Blog;
import com.example.myblog.bean.BlogWithOutUser;
import com.example.myblog.bean.PageBean;
import com.example.myblog.dao.BlogDao;
import com.example.myblog.dao.CommentDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.SumBucketPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


@Service
@Transactional  //开启事务
public class BlogService {

    @Autowired
    private BlogDao blogDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    //    @Cacheable(value = "blog",key = "#id")
    public Blog getBlogInfoById(Integer id) {
        return blogDao.findBlogDetailByBlogId(id);
    }

    //    @Cacheable(value = "blog")
    public PageInfo<Blog> pageUserBlog(String username, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Blog>(blogDao.findBlogsByUserName(username));
    }

    public void createBlog(Blog blog) {
//        blogDao.insertBlog(blog.getTitle(), blog.getContent(), blog.getAuthor().getId());
        blogDao.insertBlog(blog);
    }

    public void deleteBlogById(Integer bolgId) {
        blogDao.deleteBlogById(bolgId);
    }

    //    @CachePut(value = "blog",key = "#id")
    public Blog putBlog(Blog blog, Integer id) {
        blogDao.putBlog(blog, id);
        return blogDao.findBlogDetailByBlogId(id);
    }

    public PageInfo<Blog> getAllBlog(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Blog>(blogDao.getAllBlog());
    }


    public PageBean<BlogWithOutUser> searchBlog(String keyword, Integer pageNum, Integer pageSize) throws IOException {
        PageBean<BlogWithOutUser> pageInfo = new PageBean<>();
        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("content", keyword));
        searchSourceBuilder
                .query(boolQueryBuilder)
                .from((pageNum - 1) * pageSize)
                .size(pageSize)
                .trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        //获取返回值
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


        SearchResponse searchAll = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = search.getHits().getHits();
        System.out.println(Arrays.toString(hits));
        ArrayList<BlogWithOutUser> blogs = new ArrayList<>();
        int i = 0;
        for (int i1 = 0; i1 < hits.length; i1++) {
            Map<String, Object> sourceAsMap = hits[i].getSourceAsMap();

//
            //吧map集合转换为对象
            BlogWithOutUser blogWithOutUser = JSON.parseObject(JSON.toJSONString(sourceAsMap), BlogWithOutUser.class);
//
            blogs.add(blogWithOutUser);
        }
//        for (SearchHit hit : hits) {
////            BlogResp blogResp = new BlogResp();
//            //获取字段的集合
//            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//
////
//            //吧map集合转换为对象
//            BlogWithOutUser blogWithOutUser = JSON.parseObject(JSON.toJSONString(sourceAsMap), BlogWithOutUser.class);
////
//            blogs.add(blogWithOutUser);
//            i++;
//        }

        //封装分页
        long total = searchAll.getHits().getTotalHits().value;
        System.out.println("total=" + total);
        long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        pageInfo.setTotal(total);
        pageInfo.setList(blogs);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotalPage(totalPage);
        System.out.println(i);

        return pageInfo;

    }
}
