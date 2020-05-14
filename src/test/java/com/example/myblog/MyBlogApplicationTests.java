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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
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

}
