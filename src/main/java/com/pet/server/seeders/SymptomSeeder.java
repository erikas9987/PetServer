package com.pet.server.seeders;


import com.pet.server.model.Symptom;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SymptomSeeder {

    public static List<Symptom> seedSymptoms() {
        String[] symptomNames = {
                "Vomiting",
                "Diarrhea",
                "Lethargy",
                "Anorexia",
                "Weight Loss",
                "Dehydration",
                "Itching",
                "Sneezing",
                "Coughing",
                "Dyspnea",
                "Increased Thirst",
                "Increased Urination",
                "Ear Infection",
                "Red Eyes",
                "Nasal Discharge",
                "Skin Lesions",
                "Limping",
                "Seizures",
                "Abdominal Pain",
                "Jaundice",
                "Bad Breath",
                "Drooling",
                "Snoring",
                "Frequent Licking",
                "Hair Loss",
                "Behavioral Changes",
                "Urinary Incontinence",
                "Straining to Urinate",
                "Constipation",
                "Excessive Panting",
                "Wheezing",
                "Eye Discharge",
                "Swollen Paws",
                "Gastrointestinal Ulcers",
                "Heart Murmur",
                "High Blood Pressure",
                "Hyperthermia",
                "Hypothermia",
                "Pale Gums",
                "Rapid Heart Rate",
                "Difficulty Standing",
                "Distended Abdomen",
                "Ataxia",
                "Bloody Stool",
                "Hematuria",
                "Excessive Drooling",
                "Choking",
                "Gagging",
                "Excessive Scratching",
                "Foul Smelling Ears",
                "Tilted Head",
                "Frequent Shaking of the Head",
                "Difficulty Swallowing",
                "Excessive Salivation",
                "Pawing at Mouth or Face",
                "Subcutaneous Swelling",
                "Wounds that Don’t Heal",
                "Stiffness",
                "Increased Respiratory Rate",
                "Nail Disorders",
                "Horniness of Foot Pads",
                "Enlarged Lymph Nodes",
                "Unexplained Aggression",
                "Sudden Blindness",
                "Deafness",
                "Anxiety",
                "Depression",
                "Compulsive Behaviors",
                "Increased Vocalization",
                "Decreased Appetite",
                "Obesity",
                "Excessive Thirst or Hunger",
                "Unsteady Gait",
                "Inappropriate Elimination",
                "Recurrent Infections",
                "Weakness",
                "Collapse",
                "Shock",
                "Hypoglycemia",
                "Fever of Unknown Origin",
                "Polydipsia",
                "Polyphagia",
                "Changes in Coat Quality",
                "Nasal Congestion",
                "Increased Sleeping",
                "Heat Intolerance",
                "Cold Intolerance"
        };
        List<Symptom> symptoms = new ArrayList<>();
        for (String symptomName : symptomNames) {
            Symptom symptom = Symptom.builder()
                    .name(symptomName)
                    .build();
            symptoms.add(symptom);
        }
        return symptoms;
    }
}
