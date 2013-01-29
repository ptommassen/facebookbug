package com.example.facebookbug;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;

public class MainActivity extends Activity
{
  private static final int CONNECT_RC = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Session session = new Session(getApplicationContext());
    Session.setActiveSession(session);

    OpenRequest openRequest = new OpenRequest(this);
    openRequest.setCallback(new ConnectStatusCallback());
    List<String> permissions = new ArrayList<String>();

    openRequest.setPermissions(permissions);
    openRequest.setRequestCode(CONNECT_RC);
    session.openForRead(openRequest);
  }

  protected void upgradePermissions()
  {
    List<String> permissions = new ArrayList<String>();
    permissions.add("publish_stream");
    NewPermissionsRequest reauthorizeRequest = new NewPermissionsRequest(this, permissions);
    reauthorizeRequest.setCallback(new ReauthorizeStatusCallback());
    Session.getActiveSession().requestNewPublishPermissions(reauthorizeRequest);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
  }

  private final class ConnectStatusCallback implements StatusCallback
  {

    @Override
    public void call(Session session, SessionState state, Exception exception)
    {
      System.out.println("ConnectStatusCallback called " + state);

      if (state == SessionState.OPENED) upgradePermissions();
    }
  }

  private final class ReauthorizeStatusCallback implements StatusCallback
  {
    @Override
    public void call(Session session, SessionState state, Exception exception)
    {
      System.out.println("ReauthorizeStatusCallback called " + state);

    }
  }

}
