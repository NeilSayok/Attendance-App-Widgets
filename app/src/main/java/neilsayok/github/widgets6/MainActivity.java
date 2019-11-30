package neilsayok.github.widgets6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import neilsayok.github.attendancewidgets.DetectFaceWidget;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final boolean[] warn = {true};

//        final DetectFaceWidget df = findViewById(R.id.df);
//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                df.setWarning(!warn[0]);
//                warn[0] = !warn[0];
//            }
//        });
    }
}
