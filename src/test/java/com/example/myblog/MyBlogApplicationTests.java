package com.example.myblog;

import com.alibaba.fastjson.JSON;
import com.example.myblog.bean.Blog;
import com.example.myblog.bean.BlogWithOutUser;
import com.example.myblog.bean.Comment;
import com.example.myblog.bean.CommentWithUser;
import com.example.myblog.dao.BlogDao;
import com.example.myblog.dao.CommentDao;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.RedisService;
import com.example.myblog.service.UserService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MyBlogApplicationTests {
    @Autowired
    DataSource dataSource;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    BlogDao blogDao;
    @Autowired
    CommentDao commentDao;



    @Test
    void contextLoads() {
    }

    @Test
    public void add() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(new TimeValue(20, TimeUnit.SECONDS));
        List<BlogWithOutUser> allBlog = blogDao.getAllBlogWithOutUser();
        for (int i = 0; i < allBlog.size(); i++) {
            bulkRequest.add(new IndexRequest("blog").source(JSON.toJSONString(allBlog.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());

    }

    @Test
    public void addComment() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(new TimeValue(20, TimeUnit.SECONDS));
        List<CommentWithUser> allComment = commentDao.getAllComment();
        for (int i = 0; i < allComment.size(); i++) {
            bulkRequest.add(new IndexRequest("comment").source(JSON.toJSONString(allComment.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());

    }

    @Test
    public void test01() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();

    }
    @Test
    void test02(){
        System.out.println(redisService.getCount("aa"));
    }
    @Test
    public void test03(){
//        amqpAdmin.declareBinding(new Binding("atguigu", Binding.DestinationType.QUEUE, "exchange.direct","amqp.haha",null ));
        amqpAdmin.declareBinding(new Binding("msg_queue",Binding.DestinationType.QUEUE,"msg_exchange","msg_queue",null));
    }


    @Test
    void test04() throws IOException {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest("blog");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "title");
        searchSourceBuilder.query(matchQueryBuilder).size(3);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        int page = 1;
        while (searchHits != null && searchHits.length > 0) {

            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            try {
                searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
            if (searchHits != null && searchHits.length > 0) {
                page++;
                System.out.println("第"+page+"页");
                Arrays.asList(searchHits).stream().map(h -> h.getSourceAsString()).forEach(System.out::println);
            }
        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse;
        try {
            clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
            System.out.println("清除滚屏是否成功:" + succeeded);
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        restHighLevelClient.close();
    }
    @Test
    void test05(){

    }
}
