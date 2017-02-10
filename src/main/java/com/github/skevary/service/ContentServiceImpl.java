package com.github.skevary.service;

import com.github.skevary.model.Article;
import com.github.skevary.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("contentService")
@Transactional
public class ContentServiceImpl implements ContentService {

    private static List<Comment> comments;
    private static List<Article> articles;

    static {
        comments = populateDummyComment();
        articles = populateDummyArticles();
    }

    @Override
    public Article findById(long id) {
        for (Article article : articles) {
            if (article.getId() == id) {
                return article;
            }
        }
        return null;
    }


    @Override
    public void createNewArticle(long id, String title, String text, List<Comment> comments) {
        articles.add(new Article(id, title, text, comments));
    }

    @Override
    public void updateArticleById(Article article) {
        int index = articles.indexOf(article);
        articles.set(index, article);
    }

    @Override
    public void deleteArticleById(long id) {
        articles.removeIf(article -> article.getId() == id);
    }

    @Override
    public boolean isArticleExist(long id) {
        return findById(id) != null;
    }

    @Override
    public List<Comment> findAllCommentById(long id) {
        for (Article article : articles) {
            if (article.getId() == id) {
                return article.getComments();
            }
        }
        return null;
    }

    @Override
    public void createNewComment(long articleId, long id, String message) {
        for (Article article : articles) {
            if (article.getId() == articleId) article.getComments().add(new Comment(id, message));
        }
    }

    @Override
    public Comment findCommentById(long articleId, long id) {
        List<Comment> commentList;
        for (Article article : articles) {
            if (article.getId() == articleId) {
                commentList = article.getComments();
                for (Comment comment : commentList) {
                    if (comment.getId() == id) return comment;
                }
            }
        }
        return null;
    }

    @Override
    public void updateCommentById(long articleId, Comment comment) {
        for (Article article : articles) {
            if (article.getId() == articleId) {
                int index = article.getComments().indexOf(comment);
                article.getComments().set(index, comment);
            }
        }
    }

    @Override
    public void deleteCommentById(long articleId, long id) {
        for (Article article : articles)
            if (article.getId() == articleId)
                article.getComments().removeIf(comment -> comment.getId() == id);
    }

    private static List<Comment> populateDummyComment() {
        List<Comment> comments = new ArrayList<>();

        comments.add(new Comment(1, "First comment"));
        comments.add(new Comment(2, "Second comment"));
        comments.add(new Comment(3, "Third comment"));
        return comments;
    }

    private static List<Article> populateDummyArticles() {

        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1, "First article", "The quick brown fox jumps over the lazy dog.", comments));
        articles.add(new Article(2, "Second article", "The five boxing wizards jump quickly.", comments));
        return articles;
    }
}
