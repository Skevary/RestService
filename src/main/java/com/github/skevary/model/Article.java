package com.github.skevary.model;

import java.util.List;

public class Article {
    private long id;
    private String title;
    private String text;
    private List<Comment> comments;

    public Article() {
        id = 0;
    }

    public Article(long id, String title, String text, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;

        Article article = (Article) o;

        if (getId() != article.getId()) return false;
        if (getTitle() != null ? !getTitle().equals(article.getTitle()) : article.getTitle() != null) return false;
        if (getText() != null ? !getText().equals(article.getText()) : article.getText() != null) return false;
        return getComments() != null ? getComments().equals(article.getComments()) : article.getComments() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getText() != null ? getText().hashCode() : 0);
        result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", comments=" + comments +
                '}';
    }
}