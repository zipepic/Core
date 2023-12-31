package tokenlib.util.tokenenum;


import java.util.Date;
public enum TokenExpiration {
  ONE_HOUR(3600000),
  TEN_MINUTES(600000);
  private final long milliseconds;

  TokenExpiration(long milliseconds) {
    this.milliseconds = milliseconds;
  }

  public long getMilliseconds() {
    return milliseconds;
  }
}

