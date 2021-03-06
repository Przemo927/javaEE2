package pl.przemek.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.przemek.validation.ForbiddenWord;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@NamedQueries({
@NamedQuery(name="Comment.findAll",query="SELECT p FROM Comment p"),
@NamedQuery(name="Comment.findByDiscoveryName",query="SELECT p FROM Comment p WHERE p.discovery.name=:name"),
@NamedQuery(name="Comment.findByDiscoveryId",query="SELECT p FROM Comment p WHERE p.discovery.id=:id")})
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "comment_id",nullable = false, unique = true)
    private long id;

    @ForbiddenWord
    @Size(max=250)
    @NotNull
    @Column(nullable = false, length=250)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "discovery_id")
    private Discovery discovery;

    @NotNull
    @Column(nullable = false)
    private int upVote;
    @NotNull
    @Column(nullable = false)
    private int downVote;

    public Comment(){}
    public Comment(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.discovery=comment.getDiscovery();
        this.user = comment.getUser();
        this.upVote = comment.getUpVote();
        this.downVote = comment.getDownVote();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment comment1 = (Comment) o;

        if (id != comment1.id) return false;
        if (comment != null ? !comment.equals(comment1.comment) : comment1.comment != null) return false;
        return user != null ? user.equals(comment1.user) : comment1.user == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public Discovery getDiscovery() {
        return discovery;
    }

    public void setDiscovery(Discovery discvovery) {
        this.discovery = discvovery;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }
}
