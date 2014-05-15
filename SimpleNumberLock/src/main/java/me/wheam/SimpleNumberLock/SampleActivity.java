package me.wheam.SimpleNumberLock;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class SampleActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment())
          .commit();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.sample, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends android.app.Fragment {

    private SimpleNumberLockView lockView;

    public PlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
      return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      lockView = (SimpleNumberLockView) view.findViewById(R.id.lock_view);
      LinkedList<Integer> password = new LinkedList<Integer>();
      password.add(8);
      password.add(9);
      password.add(6);
      password.add(4);
      lockView.setPassword(password, new SimpleNumberLockView.OnFinishListener() {
        @Override
        public void onSuccess() {
          Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
          Toast.makeText(getActivity(), "fucking error", Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
