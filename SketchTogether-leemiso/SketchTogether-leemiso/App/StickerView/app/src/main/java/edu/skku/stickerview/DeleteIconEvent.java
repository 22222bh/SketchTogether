package edu.skku.stickerview;

import android.view.MotionEvent;

public class DeleteIconEvent implements edu.skku.stickerview.StickerIconEvent {
  @Override public void onActionDown(edu.skku.stickerview.StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionMove(edu.skku.stickerview.StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionUp(edu.skku.stickerview.StickerView stickerView, MotionEvent event) {
    stickerView.removeCurrentSticker();
  }
}
