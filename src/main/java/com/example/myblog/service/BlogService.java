package com.example.myblog.service;

import com.alibaba.fastjson.JSON;
import com.example.myblog.bean.Blog;
import com.example.myblog.bean.BlogWithOutUser;
import com.example.myblog.bean.PageBean;
import com.example.myblog.dao.BlogDao;
import com.example.myblog.dao.CommentDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.var;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
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
        //from-size分页
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

        SearchHit[] hits = search.getHits().getHits();
        ArrayList<BlogWithOutUser> blogs = new ArrayList<>();
        //            for (int i = 0; i < hits.length; i++) {
//                Map<String, Object> sourceAsMap = hits[i].getSourceAsMap();
////
//                //吧map集合转换为对象
//                BlogWithOutUser blogWithOutUser = JSON.parseObject(JSON.toJSONString(sourceAsMap), BlogWithOutUser.class);
////
//                blogs.add(blogWithOutUser);
//            }
        for (SearchHit hit : hits) {
            //获取字段的集合
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //吧map集合转换为对象
            BlogWithOutUser blogWithOutUser = JSON.parseObject(JSON.toJSONString(sourceAsMap), BlogWithOutUser.class);
            System.out.println(blogWithOutUser.toString());
            blogs.add(blogWithOutUser);
        }

        //封装分页
        long total = search.getHits().getTotalHits().value;
        long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        pageInfo.setTotal(total);
        pageInfo.setList(blogs);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotalPage(totalPage);
        return pageInfo;




        //sroll分页

//        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
//        SearchRequest searchRequest = new SearchRequest("blog");
//        searchRequest.scroll(scroll);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyword);
//        searchSourceBuilder.query(matchQueryBuilder).size(pageSize);
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse searchResponse = null;
//        try {
//            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
//        String scrollId = searchResponse.getScrollId();
//        SearchHit[] searchHits = searchResponse.getHits().getHits();
//        long total =searchResponse.getHits().getTotalHits().value;
//        System.out.println("total---"+total);
//
//        ArrayList<BlogWithOutUser> blogs = new ArrayList<>();
//        //第一次的不再循环中，需要单独拿出来
//        if (pageNum==1) {
//            for (SearchHit searchHit : searchHits) {
//                Map<String, Object> map = searchHit.getSourceAsMap();
//                BlogWithOutUser blog = JSON.parseObject(JSON.toJSONString(map), BlogWithOutUser.class);
//                blogs.add(blog);
//            }
//        }else {
//
//            int page = 2;
//            while (searchHits != null && searchHits.length > 0) {
//                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
//                scrollRequest.scroll(scroll);
//                try {
//                    searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                scrollId = searchResponse.getScrollId();
//                searchHits = searchResponse.getHits().getHits();
//                if (searchHits != null && searchHits.length > 0) {
//                    System.out.println("第" + page + "页");
//
//                    //吧map集合转换为对象
////
//                    if (page == pageNum) {
//                        for (SearchHit h : Arrays.asList(searchHits)) {
//                            Map<String, Object> sourceAsMap = h.getSourceAsMap();
//                            String sourceAsString = h.getSourceAsString();
//                            BlogWithOutUser blogWithOutUser = JSON.parseObject(JSON.toJSONString(sourceAsMap), BlogWithOutUser.class);
//                            blogs.add(blogWithOutUser);
//                            System.out.println(sourceAsString);
//                        }
//                        break;
//                    }
//                    page++;
//                }
//            }
//        }
//
//
////        for (SearchHit hit : hits) {
//////            BlogResp blogResp = new BlogResp();
////            //获取字段的集合
////            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
////            //吧map集合转换为对象
////            BlogWithOutUser blogWithOutUser = JSON.parseObject(JSON.toJSONString(sourceAsMap), BlogWithOutUser.class);
////            blogs.add(blogWithOutUser);
////            i++;
////        }
//        PageBean<BlogWithOutUser> pageInfo = new PageBean<>();
//        //封装分页
//
//        long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
//        System.out.println(totalPage);
//        pageInfo.setTotal(total);
//        pageInfo.setList(blogs);
//        pageInfo.setPageNum(pageNum);
//        pageInfo.setPageSize(pageSize);
//        pageInfo.setTotalPage(totalPage);
//
//        return pageInfo;
//
//    }
    }
}
