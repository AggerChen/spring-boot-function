package com.agger.springbootfunction;

import com.agger.springbootfunction.vo.Person;
import com.agger.springbootfunction.vo.ResultVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @program: RestTemplateTest
 * @description: RestTemplate测试类
 * @author: chenhx
 * @create: 2019-11-29 10:25
 **/

public class RestTemplateTest {
    RestTemplate restTemplate = null;

    @Before
    public void init() {
        restTemplate = new RestTemplate();
        //设置字符集
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    /**
     * @Title: test1
     * @Description: get请求无参
     * @author chenhx
     * @date 2019-11-29 13:12:03
     */
    @Test
    public void test1(){
        String url = "http://localhost:8080/test/getUser";

        // 1. GET方式 获取json数据
        String str = restTemplate.getForObject(url,String.class);
        System.out.println(String.format("方法一结果：%s)",str));
        Assert.assertNotNull(str);

        // 2. GET方式 获取数据并转换为指定对象ResultVO
        ResultVO result = restTemplate.getForObject(url, ResultVO.class);
        System.out.println(String.format("方法二结果：%s)",result));
        Assert.assertNotNull(result);

        // 3. GET方式 获取响应实体（包含指定对象）
        ResponseEntity<ResultVO> responseEntity = restTemplate.getForEntity(url, ResultVO.class);
        System.out.println(String.format("方法三结果：%s)",responseEntity));
        Assert.assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));

        // 4. 使用exchange() 方法执行，可以设置请求头
        // 创建请求头
        MultiValueMap header = new LinkedMultiValueMap();
        // 设置请求头content-type = application/json
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // 创建请求实体
        HttpEntity<Object> requestEntity = new HttpEntity<>(header);
        ResponseEntity<String> exchangeResult = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        System.out.println(String.format("方法四结果：%s)",exchangeResult.toString()));
        Assert.assertTrue(exchangeResult.getStatusCode().equals(HttpStatus.OK));

        // 5. 使用lambda表达式，设置请求体和响应体
        String executeResult = restTemplate.execute(url, HttpMethod.GET, request -> {
            request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }, (response) -> {
            InputStream body = response.getBody();
            byte[] bytes = new byte[body.available()];
            body.read(bytes);
            return new String(bytes);
        });

        System.out.println(String.format("方法五结果：%s)",executeResult));
    }

    /**
     * @Title: test2
     * @Description: get请求传参
     * @author chenhx
     * @date 2019-11-29 13:11:48
     */
    @Test
    public void test2(){
        String url = "http://localhost:8080/test/getUserById?id={id}";

        // 1. 将可变参数按照顺序放入，会自动匹配
        ResponseEntity<ResultVO> responseEntity = restTemplate.getForEntity(url,ResultVO.class,101);
        System.out.println(String.format("方法一结果：%s)",responseEntity));

        // 2. 将参数放入map中传入
        HashMap map = new HashMap();
        map.put("id",100);
        ResultVO forObject = restTemplate.getForObject(url, ResultVO.class, map);
        System.out.println(String.format("方法二结果：%s)",forObject));
    }

    /**
     * @Title: post_test
     * @Description: post请求 application/x-www-form-urlencoded
     * @author chenhx
     * @date 2019-11-29 13:15:15
     */
    @Test
    public void post_test(){
        String url = "http://localhost:8080/test/postUser";
        Person p  = new Person(25,"王五");

        // 方法一： 拼接form形式参数
        // 设置请求头
        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.add(HttpHeaders.CONTENT_TYPE, (MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        // 拼接参数为form形式
        String params = String.format("age=%s&name=%s",p.getAge(),p.getName());
        HttpEntity<String> request = new HttpEntity<>(params, header);
        // exchange发送Post请求
        ResponseEntity<ResultVO> exchange = restTemplate.exchange(url, HttpMethod.POST, request, ResultVO.class);
        System.out.println(String.format("POST方法一结果：%s)",exchange));

        // 方法二： map键值对
        // 设置请求头
        MultiValueMap<String, Object> map = new LinkedMultiValueMap();
        map.add("age",12);
        map.add("name","王五");
        // map键值对形式传参
        HttpEntity<MultiValueMap> request2 = new HttpEntity<>(map,header);
        // exchange发送Post请求
        ResponseEntity<ResultVO> exchange2 = restTemplate.exchange(url, HttpMethod.POST, request2, ResultVO.class);
        System.out.println(String.format("POST方法二结果：%s)",exchange2));

        // 方法三：直接调用postForObject传递map
        HttpEntity request3 = new HttpEntity<>(header);
        ResultVO resultVO = restTemplate.postForObject(url, map, ResultVO.class);
        System.out.println(String.format("POST方法三结果：%s)",resultVO));
    }

    /**
     * @Title: post_test
     * @Description: post请求 application/json
     * @author chenhx
     * @date 2019-11-29 13:15:15
     */
    @Test
    public void post_test2(){
        String url = "http://localhost:8080/test/postUser2";
        Person p  = new Person(25,"王五");

        // 方法一： 拼接form形式参数
        // 设置请求头
        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.add(HttpHeaders.CONTENT_TYPE, (MediaType.APPLICATION_JSON_VALUE));
        // 告诉服务器客户端可以处理的内容类型
        header.put(HttpHeaders.ACCEPT, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        // 拼接参数为form形式
        String params = String.format("age=%s&name=%s",p.getAge(),p.getName());
        // 直接将对象传入，内部会转换为json传递
        HttpEntity<Person> request = new HttpEntity<>(p, header);
        // exchange发送Post请求
        ResponseEntity<ResultVO> exchange = restTemplate.exchange(url, HttpMethod.POST, request, ResultVO.class);
        System.out.println(String.format("POST方法一结果：%s)",exchange));


    }

    /**
     * @Title: delete_test
     * @Description: RESTful 风格方法DELETE
     * @author chenhx
     * @date 2019-11-29 13:52:19
     */
    @Test
    public void delete_test(){
        String url = "http://localhost:8080/test/deleteUser/{id}";
        restTemplate.delete(url,120);
    }

    /**
     * @Title: testUploadFile
     * @Description: 上传文件
     * @author chenhx
     * @date 2019-11-29 14:09:56
     */
    @Test
    public void testUploadFile() {
        String url = "http://localhost:8080/test/upload";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource file = new FileSystemResource(new File("/b.txt"));
        body.add("file", file);

        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        // 设置请求头 content-type为multipart/form-data
        header.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.MULTIPART_FORM_DATA_VALUE));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, header);
        ResponseEntity<ResultVO> responseEntity = restTemplate.postForEntity(url, requestEntity, ResultVO.class);
        System.out.println("upload: " + responseEntity);
        Assert.assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
    }

}
