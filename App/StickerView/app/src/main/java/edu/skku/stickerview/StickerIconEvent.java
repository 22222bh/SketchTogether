package edu.skku.stickerview;

import android.view.MotionEvent;

public interface StickerIconEvent {
  void onActionDown(edu.skku.stickerview.StickerView stickerView, MotionEvent event);

  void onActionMove(edu.skku.stickerview.StickerView stickerView, MotionEvent event);

  void onActionUp(edu.skku.stickerview.StickerView stickerView, MotionEvent event);
}
