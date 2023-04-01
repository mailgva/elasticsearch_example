package com.horbatenko.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

@SpringBootApplication
public class ElasticsearchApplication {


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ElasticsearchApplication.class, args);

        ArticleRepository articleRepository = ctx.getBean(ArticleRepository.class);

        ElasticsearchOperations elasticsearchOperations = ctx.getBean(ElasticsearchOperations.class);

        Article article = new Article("Spring Data Elasticsearch new ");
        article.setAuthors(asList(new Author("John Alliot Smith"), new Author("John Doe")));
        System.out.println(articleRepository.save(article));

        String nameToFind = "John Smith";
        Page<Article> articleByAuthorName
                = articleRepository.findByAuthorsName(nameToFind, PageRequest.of(0, 10));
        articleByAuthorName.getContent().stream()
                .forEach(System.out::println);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(regexpQuery("title", ".*started.*"))
                .build();
        SearchHits<Article> articles =
                elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));

        articles.stream().forEach(s-> System.out.println(s.getContent()));
//
//        String articleTitle = "Spring Data Elasticsearch";
//        Query searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchQuery("title", articleTitle).minimumShouldMatch("75%"))
//                .build();
//
//        SearchHits<Article> articles =
//                elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
//        Article article = articles.getSearchHit(0).getContent();
//
//        article.setTitle("Getting started with Search Engines");
//        articleRepository.save(article);

        //articles.stream().forEach(s-> System.out.println(s.getContent()));

//        articleRepository.delete(article);

//        articleRepository.deleteById("article_id");
//
//        articleRepository.deleteByTitle("title");


    }

}
