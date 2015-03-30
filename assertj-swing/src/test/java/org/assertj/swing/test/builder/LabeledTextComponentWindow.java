/*
 * Copyright 2015 AssertJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.swing.test.builder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.test.swing.TestWindow;

/**
 * A window that contains a label and a text component.
 * @author iibarrola
 */
public class LabeledTextComponentWindow extends TestWindow {

    private final JTextField textField = new JTextField();

    public static LabeledTextComponentWindowFactory labeledTextField() {
        return new LabeledTextComponentWindowFactory();
    }

    public static class LabeledTextComponentWindowFactory {

        private String labelName;
        private String labelText;

        public LabeledTextComponentWindowFactory withLabelName(String labelName) {
            this.labelName = labelName;
            return this;
        }

        public LabeledTextComponentWindowFactory withLabelText(String labelText) {
            this.labelText = labelText;
            return this;
        }

        public LabeledTextComponentWindow createNew(final Class<?> testClass) {
            return GuiActionRunner.execute(new GuiQuery<LabeledTextComponentWindow>() {
                @Override
                protected LabeledTextComponentWindow executeInEDT() {
                    return new LabeledTextComponentWindow(testClass, labelName, labelText);
                }
            });

        }
    }

    private LabeledTextComponentWindow(Class<?> testClass, String labelName, String labelText) {
        super(testClass);
        JLabel label1 = new JLabel(labelText);
        if (labelName != null) {
            label1.setName(labelName);
        }
        textField.setName("text1");
        label1.setLabelFor(textField);
        addComponents(label1);
        addComponents(textField);
    }

    public JTextField getTextField() {
        return textField;
    }
}
