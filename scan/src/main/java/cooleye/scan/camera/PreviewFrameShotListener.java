package cooleye.scan.camera;

public interface PreviewFrameShotListener {
	public void onPreviewFrame(byte[] data, Size frameSize);
}
