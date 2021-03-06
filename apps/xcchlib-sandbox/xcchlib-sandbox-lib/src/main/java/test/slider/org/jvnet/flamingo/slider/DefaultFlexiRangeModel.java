package test.slider.org.jvnet.flamingo.slider;

import javax.swing.event.*;

public class DefaultFlexiRangeModel implements FlexiRangeModel
{
	/** The listeners waiting for model changes. */
	protected EventListenerList listenerList = new EventListenerList();

	private boolean isAdjusting = false;

	private Value value = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
    public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
    public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * Runs each {@code ChangeListener}'s {@code stateChanged}
	 * method.
	 */
	protected void fireStateChanged() {
		ChangeEvent event = new ChangeEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(event);
			}
		}
	}

	/**
	 * Returns an array of all the change listeners registered on this
	 * {@code DefaultBoundedRangeModel}.
	 * 
	 * @return all of this model's {@code ChangeListener}s or an empty
	 *         array if no change listeners are currently registered
	 * 
	 * @see #addChangeListener
	 * @see #removeChangeListener
	 */
	public ChangeListener[] getChangeListeners() {
		return /*(ChangeListener[])*/ listenerList
				.getListeners(ChangeListener.class);
	}

	private Range[] ranges;

	public DefaultFlexiRangeModel() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#setRanges(org.jvnet.flamingo.slider.FlexiRangeModel.Range[])
	 */
	@Override
    public void setRanges(Range... ranges) {
		this.ranges = new Range[ranges.length];
		// defensive array copy - so that changes in the application code
		// will not be reflected in the model
		for (int i = 0; i < ranges.length; i++) {
			this.ranges[i] = ranges[i];
		}
	}

	@Override
    public int getRangeCount() {
		return this.ranges.length;
	}
	
	@Override
    public Range getRange(int rangeIndex) {
		return this.ranges[rangeIndex];
	}
	
	protected boolean isValueLegal(Value value) {
		// try to find the value range in the range array
		boolean isRangeLegal = false;
		for (Range range : this.ranges) {
			if (range.equals(value.range)) {
				isRangeLegal = true;
			}
		}
		if (!isRangeLegal)
			return false;

		// check the range fraction
		if ((value.rangeFraction < 0.0) || (value.rangeFraction > 1.0))
			return false;

		// check discrete range
		if (value.range.isDiscrete()) {
			if ((value.rangeFraction != 0.0) && (value.rangeFraction != 1.0))
				return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#getValue()
	 */
	@Override
    public Value getValue() {
		if (this.value == null)
			return null;
		return new Value(this.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#setValue(org.jvnet.flamingo.slider.FlexiRangeModel.Value)
	 */
	@Override
    public void setValue(Value value) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("Can't pass null value");
		}
		if (!value.equals(this.value)) {
			if (!this.isValueLegal(value))
				throw new IllegalArgumentException(
						"Value is not legal for the model");
			// create copy
			this.value = new FlexiRangeModel.Value(value);
			this.fireStateChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#setValueIsAdjusting(boolean)
	 */
	@Override
    public void setValueIsAdjusting(boolean b) {
		if (this.isAdjusting != b) {
			this.isAdjusting = b;
			this.fireStateChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.slider.FlexiRangeModel#getValueIsAdjusting()
	 */
	@Override
    public boolean getValueIsAdjusting() {
		return isAdjusting;
	}
}
