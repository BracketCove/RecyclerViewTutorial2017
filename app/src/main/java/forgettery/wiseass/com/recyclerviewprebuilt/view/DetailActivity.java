package forgettery.wiseass.com.recyclerviewprebuilt.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import forgettery.wiseass.com.recyclerviewprebuilt.R;

/**
 * 18.
 *
 * This Activity simply displays Data from a ListItem in a "Detailed" View.
 * Although I'd still give it it's own Presenter/Controller/Whatever in a real App, I won't bother
 * doing that here since the Activity is so simplistic anyway.
 * Created by R_KAY on 6/3/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_DATE_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_DRAWABLE = "EXTRA_DRAWABLE";

    private TextView dateAndTime;
    private TextView message;
    private View coloredBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*I wouldn't normally pass all this Data via Intent, so understand that this is just a quick
        implementation to get things working for the Demo. I'd normally pass just a Unique id as an
        extra, and then retrieve the appropriate Data from a Service.*/
        Intent i = getIntent();
        String dateAndTimeExtra = i.getStringExtra(EXTRA_DATE_AND_TIME);
        String messageExtra = i.getStringExtra(EXTRA_MESSAGE);
        int drawableResourceExtra = i.getIntExtra(EXTRA_DRAWABLE, 0);

        dateAndTime = (TextView) findViewById(R.id.lbl_date_and_time_header);
        dateAndTime.setText(dateAndTimeExtra);

        message = (TextView) findViewById(R.id.lbl_message_body);
        message.setText(messageExtra);

        coloredBackground = findViewById(R.id.imv_colored_background);
        coloredBackground.setBackgroundResource(
                drawableResourceExtra
        );



    }
}
