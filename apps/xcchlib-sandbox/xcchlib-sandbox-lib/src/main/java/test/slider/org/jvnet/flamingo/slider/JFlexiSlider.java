/*
 * Copyright (c) 2005-2007 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package test.slider.org.jvnet.flamingo.slider;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import test.slider.org.jvnet.flamingo.slider.ui.BasicFlexiSliderUI;
import test.slider.org.jvnet.flamingo.slider.ui.FlexiSliderUI;

/**
 *
 * @author Kirill Grouchnikov
 */
public class JFlexiSlider extends JComponent
{
    private static final long serialVersionUID = 1L;
    /**
     * The UI class ID string.
     */
    private static final String uiClassID = "FlexiSliderUI";

//	/**
//	 * Sets the new UI delegate.
//	 *
//	 * @param ui
//	 *            New UI delegate.
//	 */
//	public void setUI(FlexiSliderUI ui) {
//		super.setUI(ui);
//	}

    /**
     * Resets the UI property to a value from the current look and feel.
     *
     * @see JComponent#updateUI
     */
    @Override
    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI(UIManager.getUI(this));
        } else {
            setUI(new BasicFlexiSliderUI());
        }
    }

    /**
     * Returns the UI object which implements the L&amp;F for this component.
     *
     * @return UI object which implements the L&amp;F for this component.
     * @see #setUI
     */
    public FlexiSliderUI getUI() {
        return (FlexiSliderUI) this.ui;
    }

    /**
     * Returns the name of the UI class that implements the L&amp;F for this
     * component.
     *
     * @return The name of the UI class that implements the L&amp;F for this
     *         component.
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    protected FlexiRangeModel model;

    private final Icon[] controlPointIcons;
    private final String[] controlPointTexts;

    public JFlexiSlider(final FlexiRangeModel.Range[] ranges, final Icon[] controlPointIcons,
            final String[] controlPointTexts) throws NullPointerException,
            IllegalArgumentException {
        if ((ranges == null) || (controlPointIcons == null)
                || (controlPointTexts == null)) {
            throw new NullPointerException("Parameters should be non-null");
        }

        // check parameter sizes
        final int rangeCount = ranges.length;
        if ((rangeCount != (controlPointIcons.length - 1))
                || (rangeCount != (controlPointTexts.length - 1))) {
            throw new IllegalArgumentException("Parameter sizes don't match");
        }

        this.model = new DefaultFlexiRangeModel();
        this.model.setRanges(ranges);

        this.controlPointIcons = new Icon[controlPointIcons.length];
        // defensive array copy - so that changes in the application code
        // will not be reflected in the control
        for (int i = 0; i < controlPointIcons.length; i++) {
            this.controlPointIcons[i] = controlPointIcons[i];
        }

        this.controlPointTexts = new String[controlPointTexts.length];
        // defensive array copy - so that changes in the application code
        // will not be reflected in the control
        for (int i = 0; i < controlPointTexts.length; i++) {
            this.controlPointTexts[i] = controlPointTexts[i];
        }

        this.updateUI();
    }

    public int getControlPointCount() {
        return this.model.getRangeCount()+1;
    }

    public Icon getControlPointIcon(final int controlPointIndex) {
        return this.controlPointIcons[controlPointIndex];
    }

    public String getControlPointText(final int controlPointIndex) {
        return this.controlPointTexts[controlPointIndex];
    }

    public FlexiRangeModel getModel() {
        return this.model;
    }

    public FlexiRangeModel.Value getValue() {
        return this.model.getValue();
    }

    public void setValue(final FlexiRangeModel.Value value) {
        final FlexiRangeModel m = getModel();
        final FlexiRangeModel.Value oldValue = m.getValue();
        if (value.equals(oldValue)) {
            return;
        }
        m.setValue(value);
    }
}
