package com.example.my2ndapplication;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout; // Required for FrameLayout
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private LinearLayout tabContainer;
    private ArrayList<WebView> webViewTabs;
    private ArrayList<Button> tabButtons; // To keep track of tab buttons
    private WebView activeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editText = findViewById(R.id.editText);
        tabContainer = findViewById(R.id.tabContainer);
        webViewTabs = new ArrayList<>();
        tabButtons = new ArrayList<>(); // Track the tab buttons

        Button newTabButton = findViewById(R.id.buttonNewTab);
        Button deleteTabButton = findViewById(R.id.buttonDeleteTab);

        // Load initial tab
        createNewTab();

        // Button to create new tab
        newTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTab();
            }
        });

        // Button to delete the current tab
        deleteTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCurrentTab();
            }
        });
    }

    // Create a new WebView tab
    private void createNewTab() {
        final WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        // Add the new WebView to the list of tabs
        webViewTabs.add(webView);

        // Set it as the active WebView
        setActiveWebView(webView);

        // Create a button for the new tab
        Button tabButton = new Button(this);
        tabButton.setText("Tab " + webViewTabs.size());

        Button deleteTabButton = findViewById(R.id.buttonDeleteTab);
        deleteTabButton.setVisibility(View.VISIBLE); // Ensure the button is visible


        // Add the button to the container and the list of tab buttons
        tabContainer.addView(tabButton);
        tabButtons.add(tabButton);

        // Set button click to switch to the respective WebView tab
        tabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveWebView(webView);
            }
        });

        // Load an initial page (e.g., a home page) or blank page
        webView.loadUrl("https://www.google.com");
    }

    // Set the active WebView (display the selected tab's WebView)
    private void setActiveWebView(WebView webView) {
        activeWebView = webView;

        // Replace the current WebView in the container with the selected one
        FrameLayout webViewContainer = findViewById(R.id.webViewContainer);
        webViewContainer.removeAllViews();
        webViewContainer.addView(activeWebView);
    }

    // Method to delete the current tab
    private void deleteCurrentTab() {
        if (webViewTabs.size() > 1) {
            int indexToRemove = webViewTabs.indexOf(activeWebView);

            // Remove the WebView and its corresponding tab button
            webViewTabs.remove(indexToRemove);
            tabContainer.removeView(tabButtons.get(indexToRemove));
            tabButtons.remove(indexToRemove);

            // Set the new active tab (previous tab in the list or the first one)
            if (webViewTabs.size() > 0) {
                setActiveWebView(webViewTabs.get(Math.max(0, indexToRemove - 1)));
            }
        } else {
            Toast.makeText(this, "At least one tab must remain open.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method triggered when the Go button is clicked
    public void buttonGo_Browse(View view) {
        String userInput = editText.getText().toString().trim();

        // Validate the input
        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter a valid URL or search term", Toast.LENGTH_SHORT).show();
            return;
        }

        String stringURL;

        // Check if the user input looks like a URL (starts with http, https or www)
        if (!userInput.startsWith("http://") && !userInput.startsWith("https://")) {
            if (userInput.startsWith("www.")) {
                // If starts with www, prepend https:// for better security
                stringURL = "https://" + userInput;
            } else {
                // Use a search engine for search terms
                stringURL = "https://www.google.com/search?q=" + userInput;
            }
        } else {
            stringURL = userInput;
        }

        // Load the URL in the active WebView
        activeWebView.loadUrl(stringURL);
    }
}
