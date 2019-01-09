package com.djaphar.retrofitoauthtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.djaphar.retrofitoauthtest.Interfaces.GitHubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    Context context = this;
    private String clientId = "c10e626b536cc6377952";
    private String clientSecret = "dbe275b12beef39790e4d5cb91c55cfdb7743b80";
    private String redirectUri = "futurestudio://callback";

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize"
                        + "?client_id=" + clientId + "&scope=repo&redirect_uri=" + redirectUri));
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(redirectUri)) {
            String code = uri.getQueryParameter("code");

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://github.com/")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            GitHubClient client = retrofit.create(GitHubClient.class);
            Call<AccessToken> accessTokenCall = client.getAccessToken(clientId, clientSecret, code);

            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                    if (response.body() != null) {
                        String accessToken = response.body().getAccessToken();
                        Intent intent = new Intent(context, RepoActivity.class);
                        intent.putExtra("Token", accessToken);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "no!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
