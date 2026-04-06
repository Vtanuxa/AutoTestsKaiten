package pojo;

import lombok.Data;

@Data
public class Board {
    private String title;
    private String description;
    private int top;
    private int left;
    private int default_card_type_id;
    private int right;
    private boolean first_image_is_cover;
    private boolean reset_lane_spent_time;
    private boolean third_image_is_cover;
    private boolean automove_cards;
    private boolean backward_moves_enabled;
    private boolean auto_assign_enabled;
    private int sort_order;
    private int external_id;
}
