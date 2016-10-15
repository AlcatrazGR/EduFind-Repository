package codebrains.edufind.Initializers;

/**
 * Object oriented class that represents the functionalities of a book and also contains
 * get / set methods to parse all the data.
 */
public class Book {

    private String username;
    private String title;
    private String authors;
    private String publisher;
    private String sector;
    private int amount;

    //Constructor without parameters
    public Book() {
        this.username = "";
        this.title = "";
        this.authors = "";
        this.publisher = "";
        this.sector = "";
        this.amount = 0;
    }

    //Constructor without parameters
    public Book(String us, String ti, String au, String pu, String se, int am) {
        this.username = us;
        this.title = ti;
        this.authors = au;
        this.publisher = pu;
        this.sector = se;
        this.amount = am;
    }


    public String GetUsername() {
        return this.username;
    }

    public void SetUsername(String us) {
        this.username = us;
    }

    public String GetTitle() {
        return this.title;
    }

    public void SetTitle(String ti) {
        this.title = ti;
    }

    public String GetAuthors() {
        return this.authors;
    }

    public void SetAuthors(String au) {
        this.authors = au;
    }

    public String GetPublisher() {
        return this.publisher;
    }

    public void SetPublisher(String pu) {
        this.publisher = pu;
    }

    public String GetSector() {
        return this.sector;
    }

    public void SetSector(String se) {
        this.sector = se;
    }

    public int GetBookAmount() {
        return this.amount;
    }

    public void SetBookAmount(int am) {
        this.amount = am;
    }




}
