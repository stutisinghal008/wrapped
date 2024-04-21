package com.example.cs_2340_assignment2.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.noties.markwon.Markwon;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.databinding.FragmentSecondBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.Executor;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private String api_key = "AIzaSyBd-Kgz7PaaIfikin8649X-0aNRdNdg6Cs";
    private GenerativeModel gm = new GenerativeModel("gemini-pro", api_key);

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);


        TextView textView = binding.aiInsightsContent;
        Markwon markwon = Markwon.create(getContext());


        Wrapped w = new Wrapped();
        try {
            w.convertPagingToArtist("long_term");
            w.convertPagingToTrack("long_term");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }

        Content content = new Content.Builder()
                .addText("Below are my top songs and artists. " +
                        "Please summarize the music tastes in 8 sentences and provide five song recommendations."
                        + w + "Please be brief!")
                .build();
        Executor executor = Runnable::run;

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                textView.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, executor);

        Button button = binding.regenerateButton;
        button.setOnClickListener((e) -> {
            ListenableFuture<GenerateContentResponse> response2 = model.generateContent(content);
            Futures.addCallback(response2, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    markwon.setMarkdown(textView, resultText);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, executor);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void spanSetterInView(TextView tv, String startTarget, String endTarget, int typefaceStyle, String fontFamily, int colour, float size, boolean endAtLineBreak) {
        String vString = (String) tv.getText().toString();
        int startSpan = 0, endSpan = 0;
        //Spannable spanRange = new SpannableString(vString);
        Spannable spanRange = (Spannable) tv.getText();
        while (true) {
            startSpan = vString.indexOf(startTarget, endSpan-1);     // (!@#$%) I want to check a character behind in case it is a newline
            endSpan = vString.indexOf(endTarget, startSpan+1+startTarget.length());     // But at the same time, I do not want to read the point found by startSpan. This is since startSpan may point to a initial newline. We also need to avoid the first patten matching a token from the second pattern.
            // Since this is pretty powerful, we really want to avoid overmatching it, and limit any problems to a single line. Especially if people forget to type in the closing symbol (e.g. * in bold)
            if (endAtLineBreak){
                int endSpan_linebreak = vString.indexOf("\n", startSpan+1+startTarget.length());
                if ( endSpan_linebreak < endSpan ) { endSpan = endSpan_linebreak; }
            }
            // Fix: -1 in startSpan or endSpan, indicates that the indexOf has already searched the entire string with not valid match (Lack of endspan check, occoured because of the inclusion of endTarget, which added extra complications)
            if ( (startSpan < 0) || ( endSpan < 0 ) ) break;// Need a NEW span object every loop, else it just moves the span
            // We want to also include the end "** " characters
            endSpan += endTarget.length();
            // If all is well, we shall set the styles and etc...
            if (endSpan > startSpan) {// Need to make sure that start range is always smaller than end range. (Solved! Refer to few lines above with (!@#$%) )
                spanRange.setSpan(new ForegroundColorSpan(colour), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanRange.setSpan(new RelativeSizeSpan(size), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanRange.setSpan(new StyleSpan(typefaceStyle), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Default to normal font family if settings is empty
                if( !fontFamily.equals("") )  spanRange.setSpan(new TypefaceSpan(fontFamily), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tv.setText(spanRange);
    }
}