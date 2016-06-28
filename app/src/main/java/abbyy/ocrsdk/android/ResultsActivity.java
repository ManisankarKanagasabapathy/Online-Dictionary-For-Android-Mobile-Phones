package abbyy.ocrsdk.android;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.provider.Browser;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ResultsActivity extends Activity {

	String outputPath;
    WebView browser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		browser = new WebView(this);
        setContentView(browser);

		String imageUrl = "unknown";
		
		Bundle extras = getIntent().getExtras();
		if( extras != null) {
			imageUrl = extras.getString("IMAGE_PATH" );
			outputPath = extras.getString( "RESULT_PATH" );
		}
		
		// Starting recognition process
		new AsyncProcessTask(this).execute(imageUrl, outputPath);
	}

	public void updateResults(Boolean success) {
		if (!success)
			return;
		try {
			StringBuffer contents = new StringBuffer();

			FileInputStream fis = openFileInput(outputPath);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null) {
					contents.append(text).append(System.getProperty("line.separator"));
				}
			} finally {
				fis.close();
			}

			displayMessage(contents.toString());

		} catch (Exception e) {
			displayMessage("Error: " + e.getMessage());
		}
	}

    public void displayMessage( String text )
    {
        String url = "http://en.wiktionary.org/w/index.php?title="+text+"&printable=yes";
        browser.loadUrl(url);

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}



}
