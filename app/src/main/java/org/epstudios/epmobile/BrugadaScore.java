package org.epstudios.epmobile;

/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 9/28/17.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.os.Bundle;
import android.widget.CheckBox;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrugadaScore extends RiskScore {
    private List<Integer> points = new ArrayList(Arrays.asList(35, 30, 20, 30, 20, 20, 10,
            5, 20, 10, 5, 5));
    private int ecgIndexBegin = 0;
    private int ecgIndexEnd = 2;
    private int clinicalIndexBegin = 3;
    private int clinicalIndexEnd = 7;
    private int familyIndexBegin = 8;
    private int familyIndexEnd = 10;
    private int geneticIndexBegin = 11;
    private int geneticIndexEnd = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.brugadascore);
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[12];

        // ECG
        checkBox[0] = (CheckBox) findViewById(R.id.spontaneous_type_1_ecg);
        checkBox[1] = (CheckBox) findViewById(R.id.fever_type_1_ecg);
        checkBox[2] = (CheckBox) findViewById(R.id.type_2_3_ecg);
	// Clinical history
        checkBox[3] = (CheckBox) findViewById(R.id.unexplained_arrest);
        checkBox[4] = (CheckBox) findViewById(R.id.agonal_respirations);
        checkBox[5] = (CheckBox) findViewById(R.id.arrhythmic_syncope);
        checkBox[6] = (CheckBox) findViewById(R.id.unclear_syncope);
        checkBox[7] = (CheckBox) findViewById(R.id.afl_afb);
	// Family history
        checkBox[8] = (CheckBox) findViewById(R.id.relative_definite_brugada);
        checkBox[9] = (CheckBox) findViewById(R.id.suspicious_scd);
        checkBox[10] = (CheckBox) findViewById(R.id.unexplained_scd);
	// Genetic testing
        checkBox[11] = (CheckBox) findViewById(R.id.pathogenic_mutation);
    }

    @Override
    protected void calculateResult() {
        clearSelectedRisks();
        for (CheckBox selection : checkBox) {
            if (selection.isChecked()) {
                addSelectedRisk(selection.getText().toString());
            }
        }

        int result = 0;
        int ecgScore = 0;
        int clinicalScore = 0;
        int familyScore = 0;
        int geneticScore = 0;
        for (int i = ecgIndexBegin; i <= ecgIndexEnd; ++i) {
            if (checkBox[i].isChecked() && points.get(i) > ecgScore) {
                ecgScore = points.get(i);
            }
        }
        for (int i = clinicalIndexBegin; i <= clinicalIndexEnd; ++i) {
            if (checkBox[i].isChecked() && points.get(i) > clinicalScore) {
                clinicalScore = points.get(i);
            }
        }
        for (int i = familyIndexBegin; i <= familyIndexEnd; ++i) {
            if (checkBox[i].isChecked() && points.get(i) > familyScore) {
                familyScore = points.get(i);
            }
        }
        for (int i = geneticIndexBegin; i <= geneticIndexEnd; ++i) {
            if (checkBox[i].isChecked() && points.get(i) > geneticScore) {
                geneticScore = points.get(i);
            }
        }
        String message;
        if (ecgScore == 0) {
            message = "Score requires 1 ECG finding.";
            setResultMessage(message);
        }
        else {
            result = ecgScore + clinicalScore + familyScore + geneticScore;
            message = getResultMessage(result);
        }
        displayResult(message, getString(R.string.brugada_score_title));
    }

    private String getResultMessage(int result) {
        String message = String.format("Risk Score = %.1f\n", result / 10.0);
        if (result >= 35) {
            message += "Probable/definite Brugada Syndrome";
        }
        else if (result >= 20) {
            message += "Possible Brugada Syndrome";
        }
        else {
            message += "Non-diagnostic";
        }
        setResultMessage(message);
        // no short reference added here
        return message;

    }

    @Override
    protected String getFullReference() {
        return getString(R.string.brugada_score_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.brugada_score_title);
    }

    @Override
    protected String getShortReference() {
        // no short reference given, since it is in layout
        return null;
    }
}

