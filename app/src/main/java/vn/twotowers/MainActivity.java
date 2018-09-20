package vn.twotowers;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    public static float width;
    public static float height;
    public static String PACKAGE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        //Для полноэкранного режима
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        PACKAGE_NAME = getApplicationContext().getPackageName();

        final TextView textStartGame = (TextView)findViewById(R.id.textView);


        setContentView(R.layout.main_activity);
    }

    public void onClick (View view)
    {
        if (view.getId() == R.id.textView)
            setContentView(new GameProcess(this));
    }
}
