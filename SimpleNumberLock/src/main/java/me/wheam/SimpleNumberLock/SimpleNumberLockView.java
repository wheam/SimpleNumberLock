package me.wheam.SimpleNumberLock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;


public class SimpleNumberLockView extends RelativeLayout {
  private OnFinishListener onFinishListener;
  private List<Number> numbers;
  private LinkedList<Integer> numberPassword;
  private LinkedList<Integer> inputNumbers = new LinkedList<Integer>();
  private int currentIndex = -1;
  private int errorTimes = 0;
  private TextView inputStateTextView;
  private String inputState = "";
  private TableLayout inputArea;
  private TextView deleteOne;
  private TextView clearAll;

  public SimpleNumberLockView(Context context) {
    super(context);
    init();
  }

  public SimpleNumberLockView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SimpleNumberLockView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    View.inflate(getContext(), R.layout.simple_number_lock_view, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    inputStateTextView = (TextView) findViewById(R.id.input_state);
    inputStateTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
        "fonts/con.ttf"));
    inputArea = (TableLayout) findViewById(R.id.input_layout);
    setBeginningState();
    numbers = new ArrayList<Number>();
    numbers.add(new Number(0, (TextView) findViewById(R.id.num_0)));
    numbers.add(new Number(1, (TextView) findViewById(R.id.num_1)));
    numbers.add(new Number(2, (TextView) findViewById(R.id.num_2)));
    numbers.add(new Number(3, (TextView) findViewById(R.id.num_3)));
    numbers.add(new Number(4, (TextView) findViewById(R.id.num_4)));
    numbers.add(new Number(5, (TextView) findViewById(R.id.num_5)));
    numbers.add(new Number(6, (TextView) findViewById(R.id.num_6)));
    numbers.add(new Number(7, (TextView) findViewById(R.id.num_7)));
    numbers.add(new Number(8, (TextView) findViewById(R.id.num_8)));
    numbers.add(new Number(9, (TextView) findViewById(R.id.num_9)));
    deleteOne = (TextView) findViewById(R.id.delete_one);
    deleteOne.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteOneNumber();
      }
    });
    clearAll = (TextView) findViewById(R.id.clear_all);
    clearAll.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setBeginningState();
      }
    });
  }

  public void setPassword(LinkedList<Integer> numberPassword, OnFinishListener onFinishListener) {
    if (numberPassword == null || numberPassword.isEmpty()) {
      throw new NullPointerException("null password list");
    }
    if (onFinishListener == null) {
      throw new NullPointerException("null onFinishListener");
    }
    this.onFinishListener = onFinishListener;
    this.numberPassword = numberPassword;
    initClickListener();
  }

  private void initClickListener() {
    if (numbers != null && !numbers.isEmpty()) {
      for (final Number number : numbers) {
        if (number != null) {
          number.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              input(number.getNum());
            }
          });
        }
      }
    }
  }

  private void input(int num) {
    if (currentIndex++ < 0) {
      setClearState();
    }
    if (currentIndex < numberPassword.size()) {
      inputNumbers.add(num);
    }
    setInputingState();
    if (currentIndex == numberPassword.size() - 1) {
      if (matchPassword()) {
        onFinishListener.onSuccess();
      } else {
        setErrorState();
      }
    }
  }

  private void setClearState() {
    inputState = "";
    inputNumbers.clear();
    inputStateTextView.setText(inputState);
  }

  private void setInputingState() {
    inputStateTextView.setTextColor(getContext().getResources().getColor(R.color.LimeGreen));
    StringBuilder stringBuilder = new StringBuilder();
    for (Integer number : inputNumbers) {
      stringBuilder.append(" " + number + " ");
    }
    inputState = stringBuilder.toString();
    inputStateTextView.setText(inputState);
  }

  private void setErrorState() {
    inputStateTextView.setTextColor(getContext().getResources().getColor(R.color.FireBrick));
    if (++errorTimes >= 3) {
      inputArea.setVisibility(GONE);
      inputState = getContext().getString(R.string.error_state);
      inputStateTextView.setText(inputState);
      onFinishListener.onFailed();
      return;
    }
    inputState = getContext().getString(R.string.error_state_with_retry);
    inputStateTextView.setText(inputState);
    inputNumbers.clear();
    currentIndex = -1;
  }

  private void setBeginningState() {
    currentIndex = -1;
    inputNumbers.clear();
    inputStateTextView.setTextColor(getContext().getResources().getColor(R.color.DeepSkyBlue));
    inputState = getContext().getString(R.string.beginning_state);
    inputStateTextView.setText(inputState);
  }

  private void deleteOneNumber() {
    if (inputNumbers.size() > 0 && currentIndex >= 0) {
      inputNumbers.remove(currentIndex--);
      setInputingState();
    }
    if (currentIndex < 0) {
      setBeginningState();
    }
  }

  private boolean matchPassword() {
    if (inputNumbers.size() != numberPassword.size()) {
      return false;
    } else {
      for (int i = 0; i < numberPassword.size(); i++) {
        if (numberPassword.get(i) == null) {
          throw new NullPointerException("null number in the password in the index - " + i);
        } else if (numberPassword.get(i) != null
            && !numberPassword.get(i).equals(inputNumbers.get(i))) {
          return false;
        }
      }
    }
    return true;
  }

  private class Number {
    private TextView textView;
    private int num;

    public Number(int num, TextView textView) {
      this.num = num;
      this.textView = textView;
    }

    public int getNum() {
      return num;
    }

    public TextView getTextView() {
      return textView;
    }
  }

  public interface OnFinishListener {
    void onSuccess();

    void onFailed();
  }
}
