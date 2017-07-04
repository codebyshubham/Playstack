package com.contact;

public abstract class Worker<T> {
	private Thread thread;
	private ResultListener<T> resultListener;

	public Worker() {

		System.out.println("in worker");

	}

	abstract protected T work();

	abstract protected void result(T t);

	public void doWork() {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				T t = work();
				result(t);

				if (resultListener != null) {
					resultListener.onResult(t);
				}
			}
		});
		try {
			thread.setName("I am worker thread");
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setResultListener(ResultListener<T> resultListener) {
		this.resultListener = resultListener;
	}

	public interface ResultListener<T> {
		public void onResult(T t);
	};
}
