package adilkarjauv.profrate;

public class Professor {

    private String Name = null;
    private String University = null;
    private String ImageUrl = null;
    private String Faculty = null;
    private String Count = null;
    private String sumRating = null;

    public Professor(String name, String university) {
        Name = name;
        University = university;
    }

    public Professor(String name, String university, String imageurl) {
        Name = name;
        University = university;
        ImageUrl = imageurl;
    }

    public Professor(String name, String university, String count, String sumRating, String imageUrl) {
        Name = name;
        University = university;
        Count = count;
        this.sumRating = sumRating;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUniversity() {
        return University;
    }

    public void setUniversity(String university) {
        University = university;
    }

    public String getImageUrl() {return ImageUrl;}

    public void setImageUrl(String imageurl) { ImageUrl = imageurl; }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getSumRating() {
        return sumRating;
    }

    public void setSumRating(String sumRating) {
        this.sumRating = sumRating;
    }
}
