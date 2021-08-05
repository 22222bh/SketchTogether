package edu.skku.stickerview;

public class FlipHorizontallyEvent extends AbstractFlipEvent {

  @Override @edu.skku.stickerview.StickerView.Flip protected int getFlipDirection() {
    return edu.skku.stickerview.StickerView.FLIP_HORIZONTALLY;
  }
}
