package com.github.skevary.service;

import com.github.skevary.model.Article;
import com.github.skevary.model.Comment;

import java.util.List;

public interface ContentService {
    Article findById(long id);

    void createNewArticle(long id, String title, String text, List<Comment> comments);

    void updateArticleById(Article article);

    void deleteArticleById(long id);

    public boolean isArticleExist(long id);

    List<Comment> findAllCommentById(long id);

    void createNewComment(long articleId, long id, String message);

    Comment findCommentById(long articleId, long id);

    void updateCommentById(long articleId, Comment comment);

    void deleteCommentById(long articleId, long id);
}
