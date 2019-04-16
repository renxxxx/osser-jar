package oss.launcher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

public class Config {

	ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
	OssLauncherTask ossLauncherTask = new OssLauncherTask(this);
	RealizeTmpFileTask realizeTmpFileTask = new RealizeTmpFileTask(this);
	DeleteFileTask deleteFileTask = new DeleteFileTask(this);
	DeleteTmpTask deleteTmpTask = new DeleteTmpTask(this);
	DataSource dataSource = null;
	String environment;
	String project;
	String webroot;

	public Config(DataSource dataSource, String environment, String project, String webroot) {
		if (!"prod".equals(environment) && !"test".equals(environment) && !"dev".equals(environment))
			throw new RuntimeException("environment有误");
		this.dataSource = dataSource;
		this.environment = environment;
		this.webroot = webroot;
		this.project = project;

		scheduledExecutorService.execute(ossLauncherTask);
		scheduledExecutorService.scheduleWithFixedDelay(realizeTmpFileTask, 0, 1, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(deleteFileTask, 0, 1, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(deleteTmpTask, 0, 1, TimeUnit.MINUTES);
	}

}
