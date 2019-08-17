package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private long id;
    private String title;
    private String shortText;
    private String text;
    private Date createdDate;
    private Category category;
    private String picUrl;
    private User user;


}
