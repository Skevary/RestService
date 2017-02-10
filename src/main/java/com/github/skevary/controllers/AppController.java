package com.github.skevary.controllers;

import com.github.skevary.model.Article;
import com.github.skevary.model.Comment;
import com.github.skevary.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    ContentService contentService; //Service which will do all data retrieval/manipulation work

    //-------------------Find Article by Id--------------------------------------------------------

    @RequestMapping(value = "/content/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> getArticle(@PathVariable("id") long id) {
        System.out.println("Fetching Article with id " + id);
        Article article = contentService.findById(id);
        if (article == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<Article>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Article>(article, HttpStatus.OK);
    }

    //-------------------Create a new Article--------------------------------------------------------
    @Secured({"USER" ,"ROLE_ADMIN" })
    @RequestMapping(value = "/content", method = RequestMethod.POST)
    public ResponseEntity<Void> createArticle(@RequestBody Article article, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Article " + article.getTitle());
        if (contentService.isArticleExist(article.getId())) {
            System.out.println("A Article with id " + article.getId() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        contentService.createNewArticle(article.getId(), article.getTitle(), article.getText(), article.getComments());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/content/{id}").buildAndExpand(article.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //------------------- Update a Article --------------------------------------------------------
    @Secured({"USER" ,"ROLE_ADMIN" })
    @RequestMapping(value = "/content/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id, @RequestBody Article article) {
        System.out.println("Updating Article " + id);

        Article currentArticle = contentService.findById(id);

        if (currentArticle == null) {
            System.out.println("Article with id " + id + " not found");
            return new ResponseEntity<Article>(HttpStatus.NOT_FOUND);
        }
        currentArticle.setTitle(article.getTitle());
        currentArticle.setText(article.getText());
        currentArticle.setComments(article.getComments());

        contentService.updateArticleById(currentArticle);
        return new ResponseEntity<Article>(currentArticle, HttpStatus.OK);
    }

    //------------------- Delete a Article --------------------------------------------------------
    @Secured({ "ROLE_ADMIN" })
    @RequestMapping(value = "/content/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Article> deleteArticle(@PathVariable("id") long id) {
        System.out.println("Deleting Article with id " + id);

        Article article = contentService.findById(id);
        if (article == null) {
            System.out.println("Unable to delete. Article with id " + id + " not found");
            return new ResponseEntity<Article>(HttpStatus.NOT_FOUND);
        }
        contentService.deleteArticleById(id);
        return new ResponseEntity<Article>(HttpStatus.NO_CONTENT);
    }

    //-------------------Find all Comment by Id--------------------------------------------------------

    @RequestMapping(value = "/content/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getAllComment(@PathVariable("id") long id) {
        System.out.println("Fetching Article with id " + id);
        List<Comment> comments = contentService.findAllCommentById(id);
        if (comments.isEmpty()) {
            System.out.println("Comments on the article " + id + " not found");
            return new ResponseEntity<List<Comment>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
    }

    //-------------------Create a new Comment by Article--------------------------------------------------------
    @Secured({"USER" ,"ROLE_ADMIN" })
    @RequestMapping(value = "/content/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<Void> createComment(@PathVariable("id") long id, @RequestBody Comment comment, UriComponentsBuilder ucBuilder) {
        if (!contentService.isArticleExist(id)) {
            System.out.println("A Article with id " + id + " not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Creating Comment " + comment.getId());

        contentService.createNewComment(id, comment.getId(), comment.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/comments/{id}").buildAndExpand(comment.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //------------------- Update a Comment --------------------------------------------------------
    @Secured({"USER" ,"ROLE_ADMIN" })
    @RequestMapping(value = "/content/{id}/comments/{commId}", method = RequestMethod.PUT)
    public ResponseEntity<Comment> updateComment(@PathVariable("id") long id, @PathVariable("commId") long commId, @RequestBody Comment comment) {
        if (!contentService.isArticleExist(id)) {
            System.out.println("A Article with id " + id + " not found");
            return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Updating Comment id " + commId + " width Article id " + id);

        Comment currentComment = contentService.findCommentById(id, commId);

        if (currentComment == null) {
            System.out.println("Comment with id " + commId + " not found");
            return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
        }
        currentComment.setId(comment.getId());
        currentComment.setMessage(comment.getMessage());

        contentService.updateCommentById(id, currentComment);
        return new ResponseEntity<Comment>(currentComment, HttpStatus.OK);
    }

    //------------------- Delete a Comment --------------------------------------------------------
    @Secured({ "ROLE_ADMIN" })
    @RequestMapping(value = "/content/{id}/comments/{commId}", method = RequestMethod.DELETE)
    public ResponseEntity<Comment> deleteComment(@PathVariable("id") long id,@PathVariable("commId") long commId) {
        if (!contentService.isArticleExist(id)) {
            System.out.println("A Article with id " + id + " not found");
            return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Deleting Comment with id " + commId + "width Article id " + id);
        Comment comment = contentService.findCommentById(id,commId);
        if (comment == null) {
            System.out.println("Unable to delete. Comment with id " + commId + " not found");
            return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
        }
        contentService.deleteCommentById(id,commId);
        return new ResponseEntity<Comment>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = {"/", "/index**"}, method = {RequestMethod.GET})
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        model.addObject("title", "Home page");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}