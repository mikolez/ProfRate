package adilkarjauv.profrate;


public class Comment {

    String id = null;
    String prof = null;
    String user = null;
    String text = null;
    String time = null;
    String rating = null;

    public Comment(String id, String prof, String text) {
        this.id = id;
        this.prof = prof;
        this.text = text;
    }

    public Comment(String id, String prof, String text, String time) {
        this.id = id;
        this.prof = prof;
        this.text = text;
        this.time = time;
    }

    public Comment(String id, String prof, String text, String time, String rating) {
        this.id = id;
        this.prof = prof;
        this.text = text;
        this.time = time;
        this.rating = rating;
    }

    public Comment(String id, String prof, String text, String time, String rating, String user) {
        this.id = id;
        this.prof = prof;
        this.text = text;
        this.user = user;
        this.rating = rating;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
