package challenge.concurrency;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class GoodInterval {

    private static final Logger logger = Logger.getLogger(GoodInterval.class.getName());

    private static class ImmutableInterval {

        final int left;
        final int right;

        private ImmutableInterval(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    private final AtomicReference<ImmutableInterval> intervalReference
            = new AtomicReference<>(new ImmutableInterval(0, 0));

    public int getLeft() {
        return intervalReference.get().left;
    }

    public void setLeft(int newLeft) {

        while (true) {
            ImmutableInterval interval = intervalReference.get();
            if (newLeft > interval.right) {
                // logger.warning("Left cannot be bigger than right");
                return;
            }

            ImmutableInterval newInterval = new ImmutableInterval(newLeft, interval.right);
            if (intervalReference.compareAndSet(interval, newInterval)) {
                return;
            }
        }
    }

    public int getRight() {
        return intervalReference.get().right;
    }

    public void setRight(int newRight) {

        while (true) {
            ImmutableInterval interval = intervalReference.get();
            if (newRight < interval.left) {
                // logger.warning("Right cannot be smaller than left");
                return;
            }

            ImmutableInterval newInterval = new ImmutableInterval(interval.left, newRight);
            if (intervalReference.compareAndSet(interval, newInterval)) {
                return;
            }
        }
    }
}
