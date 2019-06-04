package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

// todo: Dealing with BombBoxes
public class BombBox extends Box {

    public BombBox(int startingX, int startingY, Texture image) {
        super(startingX, startingY, image);

        initBombBox();
    }

    private void initBombBox() {
    }

//    private boolean bombBoxCollision(BombBox bb) {
//
//        for (BombBox otherBB : this.bombBoxes) {
//            if (bb.isBottomCollision(otherBB) ||
//                    bb.isTopCollision(otherBB) ||
//                    bb.isLeftCollision(otherBB) ||
//                    bb.isRightCollision(otherBB)) {
//                return true;
//            }
//        }
//        return false;
//    }

//    private BombBox bombBoxCollider(BombBox bb) {
//
//        for (BombBox otherBB : this.bombBoxes) {
//            if (bb.isBottomCollision(otherBB) ||
//                    bb.isTopCollision(otherBB) ||
//                    bb.isLeftCollision(otherBB) ||
//                    bb.isRightCollision(otherBB)) {
//                return BombBox;
//            }
//        }
//        return null;
//    }
//    public boolean checkForExplosion() {
//
//        for (BombBox bb : this.bombBoxes) {
//            if (bombBoxCollision(bb)) {
//                explode(bb);
//                bombBoxCollider(bb)
//            }
//        }
//
//    }

//    private void explode(BombBox bb) {
//
//        int incrementTo = this.walls.size();
//
//        for (int i = 0; i < incrementTo; i++) {
//            if(bb.isBottomCollision(wall) ||
//                    bb.isTopCollision(wall) ||
//                    bb.isLeftCollision(wall) ||
//                    bb.isRightCollision(wall))
//            this.walls = ArrayUtils.removeElement(array, element);
//            incrementTo = incrementTo - 1;
//        }
//    }

}
