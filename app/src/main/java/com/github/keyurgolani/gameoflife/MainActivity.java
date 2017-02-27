package com.github.keyurgolani.gameoflife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridCanvasView mGridCanvasView;
    private Button mResetButton;
    private Button mNextButton;
    private Button mResizeButton;
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridCanvasView = (GridCanvasView) findViewById(R.id.grid_canvas_view);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mResizeButton = (Button) findViewById(R.id.resize_button);

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Reset?")
                        .setMessage("Do you really want to destroy the earth and start over?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(MainActivity.this, "Earth destroyed! Starting over!", Toast.LENGTH_LONG).show();
                                mGridCanvasView.clearCanvas();
                            }})
                        .setNegativeButton("No", null).show();
            }
        });

        mResizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Resize?")
                        .setMessage("Resizing will clear the earth of all the lives. Want to start over?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(MainActivity.this, "Earth destroyed! Starting over!", Toast.LENGTH_LONG).show();
                                mGridCanvasView.nextSize();
                            }})
                        .setNegativeButton("No", null).show();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextGeneration();
            }
        });
    }

    private void getNextGeneration() {
        LifeUtils nextGeneration = mGridCanvasView.currentLife.getDuplicateLife();
        for(int i = 0; i < nextGeneration.getHeight(); i++) {
            for(int j = 0; j < nextGeneration.getWidth(); j++) {
                int surroundingAliveCount = 0;
                if(j > 0) {
                    // Check Bottom Cell
                    if(mGridCanvasView.currentLife.isAlive(j - 1, i)) {
                        surroundingAliveCount++;
                    }
                }
                if(j < nextGeneration.getWidth() - 1) {
                    // Check Top Cell
                    if(mGridCanvasView.currentLife.isAlive(j + 1, i)) {
                        surroundingAliveCount++;
                    }
                }
                if(i > 0) {
                    // Check Left Cell
                    if(mGridCanvasView.currentLife.isAlive(j, i - 1)) {
                        surroundingAliveCount++;
                    }
                }
                if(i < nextGeneration.getHeight() - 1) {
                    // Check Right Cell
                    if(mGridCanvasView.currentLife.isAlive(j, i + 1)) {
                        surroundingAliveCount++;
                    }
                }
                if(j > 0 && i > 0) {
                    // Check Top-Right Cell
                    if(mGridCanvasView.currentLife.isAlive(j - 1, i - 1)) {
                        surroundingAliveCount++;
                    }
                }
                if(j < nextGeneration.getWidth() - 1 && i > 0) {
                    // Check Bottom-Left Cell
                    if(mGridCanvasView.currentLife.isAlive(j + 1, i - 1)) {
                        surroundingAliveCount++;
                    }
                }
                if(j > 0 && i < nextGeneration.getHeight() - 1) {
                    // Check Bottom-Right Cell
                    if(mGridCanvasView.currentLife.isAlive(j - 1, i + 1)) {
                        surroundingAliveCount++;
                    }
                }
                if(j < nextGeneration.getWidth() - 1 && i < nextGeneration.getHeight() - 1) {
                    // Check Top-Left Cell
                    if(mGridCanvasView.currentLife.isAlive(j + 1, i + 1)) {
                        surroundingAliveCount++;
                    }
                }

                // Take Action
                if(mGridCanvasView.currentLife.isAlive(j, i)) {
                    if(surroundingAliveCount < 2 || surroundingAliveCount > 3) {
                        nextGeneration.killCell(j, i);
                    }
                } else {
                    if(surroundingAliveCount == 3) {
                        nextGeneration.breedCell(j, i);
                    }
                }
            }
        }
        for(int i = 0; i < nextGeneration.getHeight(); i++) {
            for (int j = 0; j < nextGeneration.getWidth(); j++) {
                mGridCanvasView.currentLife.setCell(j, i, nextGeneration.isAlive(j, i));
            }
        }
        mGridCanvasView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play, menu);
        return true;
    }

    public class WaitTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // Continue
            }
            return params[0] - 1;
        }

        @Override
        protected void onPostExecute(Integer count) {
            getNextGeneration();
            if(count > 1) {
                new WaitTask().execute(count);
            }
            super.onPostExecute(count);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.play_next_five:
                getNextGeneration();
                new WaitTask().execute(5);
                return true;
            case R.id.play_next_ten:
                getNextGeneration();
                new WaitTask().execute(10);
                return true;
            case R.id.play_next_twenty:
                getNextGeneration();
                new WaitTask().execute(20);
                return true;
            case R.id.play_next_thirty:
                getNextGeneration();
                new WaitTask().execute(30);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Game?")
                .setMessage("Are you sure you want to exit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
