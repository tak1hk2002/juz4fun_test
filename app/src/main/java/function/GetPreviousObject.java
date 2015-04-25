package function;

import java.io.Serializable;

/**
 * Created by LAM on 23/4/2015.
 */
//Temporarily store an object and pass it to next activity
@SuppressWarnings("serial")
public class GetPreviousObject implements Serializable {

    public GetPreviousObject(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;


}
