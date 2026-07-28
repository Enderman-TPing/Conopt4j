# Progress Bar

> This function will not take effect as a friendly fallback when your application is running in a non-interactive environment (like a CI server or with redirected input/output). Do not shift the terminal window size while the function is running in an interactive environment.

You can use the `ProgressBar` class to create progress bars.

### Style

Each of the progress bars is shown like:

\<prefix (e.g. Loading.../Downloading xxx...)> <font color=green>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</font><font color=blue>────────────────────</font>\<suffix (e.g. 60% remaining time: 12 : 34)>

### Example Code

``` java
ProgressBar pb=new ProgressBar();//initialize
pb.setPrefix("Loading");
int progress = 0;
pb.setProgress(0);
pb.setSuffix("0%");
pb.show();
while(progress<=100){//if progress>100 an exception will be thrown
    pb.setProgress(++progress);
    pb.setSuffix(progress+"%");
    ProgressBar.update();
    Thread.sleep(1000);
}
pb.hide();
```

You may add multiple progress bars.



