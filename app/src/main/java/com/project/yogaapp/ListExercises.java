package com.project.yogaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.project.yogaapp.Adapter.RecyclerViewAdapter;
import com.project.yogaapp.Database.DBManager;
import com.project.yogaapp.Model.Exercise;
import com.project.yogaapp.R;

import java.util.ArrayList;
import java.util.List;

public class ListExercises extends AppCompatActivity {
    List<Exercise> exerciseList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exercises);
//        insertDb();
        initData();
        recyclerView = findViewById(R.id.listExercise);
        adapter = new RecyclerViewAdapter(exerciseList, getBaseContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        int imgseqCounter = 1;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                String imgName = "yoga" + imgseqCounter;
                String mDrawableName = "yoga" + imgseqCounter;
                int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                exerciseList.add(new Exercise(resID, name, desc));
                imgseqCounter++;
            } while (cursor.moveToNext());
        } else {
            insertDb();
        }
        cursor.close();
    }

    private void insertDb() {
        //test code to create  table and  db
        dbManager = new DBManager(this);
        dbManager.open();
        dbManager.insert("Bridge – Bandha Sarvangasana", "The Bridge yoga pose is a great front hip joints opener, it also strengthens your spine, opens the chest, and improves your spinal flexibility in addition to stimulating your thyroid");
        dbManager.insert("Downward Dog – Adho Mukha Svanasana", "The Downward Dog yoga pose lengthens and decompresses the spine, stretches the hamstrings, strengthens your arms, flushes your brain with fresh oxygen and calms your mind.");
        dbManager.insert("Child Pose – Balasana", "The Child Pose is a resting pose useful to relieve neck, back and hip strain. While in the posture you should have slow are regulated breath; extended arms; resting hips and your forehead should be touching the mat.");
        dbManager.insert("Easy Pose – Sukhasana", "The Easy Pose may seem as an easy pose but it has many benefits for the body. For example, it is a hip opener, it is calming, and it eases the menstrual pain for women in addition to lowering the level of anxiety.");
        dbManager.insert("Warrior 1 – Virabhadrasana I", "The Warrior I is a great pose for those of you who have had a hectic day at work and just need to relax your body and mind.");
        dbManager.insert("Warrior 2 – Virabhadrasana II", "The Warrior II yoga pose also strengthens your legs and arms, opens your chest and shoulders, and it contracts your abdominal organs. Your breath needs to be regulated, your focus should be on the expansion of your arms which will help you to improve your patience.");
        initData();
    }
}
