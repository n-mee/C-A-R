package repository;

import model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository {
    private static final String DATA = """
MONDAY|4-4|7:00|12:00|NSTP 1|CRIM 1B / 1A|Mr. Pahitong
MONDAY|4-5|7:00|12:00|Intro to Crim.|BSC. 1C / 1B|Mr. Monte
MONDAY|4-6|7:00|9:30|OJT Consultation|BSC. 4|Dr. Ramos
MONDAY|4-8|7:00|12:00|Forensic Chemistry|BSC 3A|Mrs. F. Francisco
MONDAY|4-7|9:30|12:00|Fund. of Martial Arts|BSC 1D|Mr. Alivar
MONDAY|4-11|7:00|9:30|PE3|BSIT 2A|Ms. Hadloc
MONDAY|4-11|9:30|12:00|Retorika|BSIT 2A|Ms. Tiongson
MONDAY|4-4|1:30|4:00|NSTP 1|CRIM 1D|Mr. Pahitong
MONDAY|4-5|1:30|4:00|Intro to Crim.|BSC. 1A|Mr. Monte
MONDAY|4-6|1:30|4:00|The Contemporary World|BSC. 1B|Dr. Dillena
MONDAY|4-8|1:30|4:00|Forensic Chemistry|BSC 3B|Mrs. F. Francisco
MONDAY|4-11|1:30|4:00|Retorika|BSIT 2B|Ms. Tiongson
MONDAY|4-12|1:30|4:00|Linear Algebra|BSIT 3B|Mr. Frondozo
MONDAY|4-13|1:30|4:00|Sys. Analysis & Design|BSIT 3A|Mr. Acebuche
MONDAY|4-4|4:00|7:30|NSTP 1|CRIM 1C|Mr. Pahitong
MONDAY|4-5|4:00|7:30|Sining ng Kom.|CRIM 1B|Mrs. Peralta
MONDAY|4-6|4:00|7:30|The Contemporary World|BSC. 1A|Dr. Dillena
MONDAY|4-7|4:00|7:30|Readings in Phil. History|BSC. 1D|Dr. Ja. Reyes
MONDAY|4-11|4:00|7:30|Rizal|BSIT 2D|Mr. Cortez
MONDAY|4-12|4:00|7:30|Linear Algebra|BSIT 3A|Mr. Frondozo
MONDAY|4-13|4:00|7:30|Sys. Analysis & Design|BSIT 3B|Mr. Acebuche   3RD FLOOR COLLEGE BUILDING Room Time Subject / Section Professor
MONDAY|3-1|10:00|12:00|History of Math / College & Adv. Algebra|BSE Math 1|Ms. L. Umipig
MONDAY|3-2|10:00|12:00|Calculus 1|BSE Math 2|Mr. Figueroa
MONDAY|3-1|2:00|4:00|Purposive Comm.|BSE 2|Ms. Ronsable
MONDAY|3-2|2:00|4:00|Calculus 3|BSE Math 3|Mr. Figueroa
MONDAY|3-5|2:00|4:00|PE1|BSBA 1A|Mr. Valdez
MONDAY|3-5|4:00|5:30|PE1|BSIT 1B|Mr. Valdez
MONDAY|3-5|5:30|7:30|PE3|BSBA 2B|Mr. Valdez
MONDAY|3-6|4:00|6:30|Understanding the Self|BSBA 1A|Mrs. Del Rosario
MONDAY|3-6|5:30|7:30|Math in the Modern World|BSBA 1B|Mrs. Umipig
MONDAY|3-9|7:30|12:00|Macro Pers.|HM 1B / 1A / 1C / 1D|Ms. Villaverde
MONDAY|3-10|10:00|12:00|PE1|BSHM 1C|Ms. Hadloc
MONDAY|3-10|2:00|4:00|PE1|BSHM 1A|Mr. V. Santos
MONDAY|3-10|4:00|6:30|PE1|BSHM 1B|Mr. V. Santos
MONDAY|3-11|7:30|9:30|STS The Entrep.|BSHM 2B|Mr. Retiro
MONDAY|3-11|12:30|3:00|OLLC Ethics|BSHM 1C|Mr. Cortez
MONDAY|3-11|4:00|6:30|Readings in Phil. History|BSHM 1C|Mrs. Zipagan
MONDAY|3-12|7:30|12:00|Asian Cuisine|BSHM 2C / 2B|Mrs. Caparuzo
MONDAY|3-12|2:00|4:00|OLLC Ethics|BSHM 2A|Mr. Cortez
MONDAY|3-12|4:00|6:30|Asian Cuisine|BSHM 2D|Mrs. Caparuzo   
TUESDAY|3-1|10:00|12:00|Assessment 1|EDUC 3|Dr. Figueroa
TUESDAY|3-2|10:00|12:00|OLLC Culture & Ethics|EDUC 1|Mr. Cortez
TUESDAY|3-6|7:30|9:30|Income Tax|BSBA 2B|Mr. Pike
TUESDAY|3-6|10:00|12:00|Business Law 1|BSBA 2B|Mr. Pike
TUESDAY|3-5|1:30|3:30|The Contemporary World|BSBA 1A|Dr. Dillena
TUESDAY|3-6|1:30|3:30|Business Law 1|BSBA 2A|Mr. Pike
TUESDAY|3-8|7:30|9:30|Understanding the Self|BSBA 1B|Mr. Millanes
TUESDAY|3-8|10:00|12:00|OLLC Culture & Ethics|BSBA 1A|Mrs. Umipig
TUESDAY|3-8|1:30|3:30|NSTP 1|BSBA 1B|Mrs. Cabrera
TUESDAY|3-8|4:00|6:30|Great Books|BSBA 2A|Mrs. Umipig
TUESDAY|3-9|7:30|9:30|Bar & Bev. Mgt.|HM 3A|Ms. Villaverde
TUESDAY|3-9|10:00|12:00|Bar & Bev. Mgt.|HM 3C|Ms. Villaverde
TUESDAY|3-9|4:00|6:30|Gastro (Food & Culture)|HM 3C|Ms. Villaverde
TUESDAY|3-10|7:30|9:30|Food Styling|BSHM 2D|Mrs. V. Castillo
TUESDAY|3-10|10:00|12:00|Food Styling|BSHM 2E|Mrs. V. Castillo
TUESDAY|3-11|7:30|9:30|STS The Entrep.|BSHM 2E|Mr. Retiro
TUESDAY|3-11|10:00|12:00|Applied Bus. Tools|BSHM 2E|Ms. C. Diamco
TUESDAY|3-12|7:30|9:30|Applied Bus. Tools|HM 3B|Mr. Villalon
TUESDAY|3-12|1:30|3:30|Applied Bus. Tools|BSHM 2C|Ms. C. Diamco
TUESDAY|3-12|4:00|6:30|Understanding the Self|BSHM 1B|Mrs. Del Rosario
TUESDAY|3-1|4:00|6:30|Teaching Social Studies in Elem. 1|EDUC 3|Dr. Ja. Reyes
TUESDAY|3-2|4:00|6:30|World Lit|BSE 3|Dr. Urmaza
TUESDAY|3-3|4:00|6:30|Rizal|EDUC 2|Mr. Cortez
TUESDAY|3-5|4:00|6:30|PE3|BSIT 2C / 2B|Mr. Valdez   4TH FLOOR COLLEGE BUILDING Room Time Subject / Section Professor
TUESDAY|4-4|7:30|9:30|Traffic Mgt. & Acci. Dent.|CRIM 3A|Mr. Tumalip
TUESDAY|4-5|7:30|9:30|Law Enforcement Org. & Admin.|CRIM 2E|Mr. Noceda
TUESDAY|4-6|7:30|9:30|Theories & Causation|CRIM 2B|Dr. Ramos
TUESDAY|4-8|7:30|10:00|Forensic Chemistry|BSC 3E|Mrs. F. Francisco
TUESDAY|4-11|7:30|9:30|Philippine Lit.|BSCRIM 2A|Ms. Tiongson
TUESDAY|4-12|7:30|9:30|PE1|BSIT 1C|Ms. Hadloc
TUESDAY|4-4|9:30|12:00|Traffic Mgt. & Acci. Dent.|CRIM 3B|Mr. Tumalip
TUESDAY|4-7|9:30|12:00|Fund. of Martial Arts|BSC 1C|Mr. Alivar
TUESDAY|4-11|9:30|12:00|Readings in Phil. History|BSIT 1C|Ms. Tiongson
TUESDAY|4-4|1:30|4:00|Traffic Mgt. & Acci. Dent.|CRIM 3E|Mr. Tumalip
TUESDAY|4-6|1:30|4:00|The Contemporary World|BSC. 1D|Dr. Malabanan
TUESDAY|4-7|1:30|4:00|First Aid & Water Surv.|BSC 2E|Mr. Legacion
TUESDAY|4-8|1:30|5:30|Forensic Chemistry|BSC 3C|Mrs. F. Francisco
TUESDAY|4-12|1:30|4:00|Comp. Prog 2|BSIT 2B|Mr. Acebuche
TUESDAY|4-13|1:30|4:00|NSTP 1|BSIT 1B|Mr. Cabrera
TUESDAY|4-4|4:00|7:30|Traffic Mgt. & Acci. Dent.|CRIM 3D|Mr. Tumalip
TUESDAY|4-5|4:00|7:30|Sining ng Kom.|CRIM 1D|Mrs. Peralta
TUESDAY|4-6|4:00|7:30|The Contemporary World|BSC. 1C|Dr. Malabanan
TUESDAY|4-12|4:00|7:30|IT Tech Writing|BSIT 3B|Ms. F. San Miguel
TUESDAY|4-13|4:00|7:30|Comp. Prog 2|BSIT 2A|Mr. Acebuche
TUESDAY|4-14|4:00|7:30|The Contemporary World|BSIT 1B|Mrs. M. Zipagan   
WEDNESDAY|4-4|7:30|9:30|Comparative Models|CRIM 2A|Mr. Monte
WEDNESDAY|4-5|7:30|9:30|Law Enforcement Op. & Plan|CRIM 3B|Mr. Noceda
WEDNESDAY|4-6|7:30|9:30|Non Institution|CRIM 3B|Mr. Llorando
WEDNESDAY|4-11|7:30|9:30|Philippine Lit.|BSIT 2D|Ms. Tiongson
WEDNESDAY|4-4|9:30|12:00|Fund. of Martial Arts|BSC 1C|Mr. Alivar
WEDNESDAY|4-5|9:30|12:00|Comparative Policing|CRIM 2B|Mr. Noceda
WEDNESDAY|4-6|9:30|12:00|Law Enforcement Op. & Plan|CRIM 3A|Mr. Llorando
WEDNESDAY|4-11|9:30|12:00|Retorika|BSIT 2D|Ms. Tiongson
WEDNESDAY|4-7|9:30|12:00|First Aid & Water Surv.|BSC 2D|Mr. Legacion
WEDNESDAY|4-8|9:30|12:00|Math & Science Tech|BSIT 2D|Mr. Duldulao
WEDNESDAY|4-4|1:30|4:00|Understanding the Self|BSCRIM 2B|Mrs. De Ola
WEDNESDAY|4-5|1:30|4:00|Rizal|BSCRIM 2E|Mr. Reyes
WEDNESDAY|4-6|1:30|4:00|Law Enforcement Op.|CRIM 3D|Mr. Pahitong
WEDNESDAY|4-8|1:30|4:00|Math & Science Tech|BSIT 2H|Mr. Duldulao
WEDNESDAY|4-9|1:30|4:00|The Contemporary World|BSIT 2F|Dr. Dillena
WEDNESDAY|4-12|1:30|4:00|Emerging Tech|BSIT 2C|Mr. Acebuche
WEDNESDAY|4-13|1:30|4:00|NSTP 1|BSIT 1A|Mr. Cabrera
WEDNESDAY|4-8|4:00|7:30|The Contemporary World|BSIT 1B|Mrs. Zipagan
WEDNESDAY|4-9|4:00|7:30|Rizal|BSIT 2F|Mr. Tiongson
WEDNESDAY|4-11|4:00|7:30|The Contemporary World|BSIT 1B|Mrs. Zipagan
WEDNESDAY|4-12|4:00|7:30|Emerging Tech|BSIT 2J|Mr. Acebuche
WEDNESDAY|4-13|4:00|7:30|NSTP 1|BSIT 1A|Mr. Cabrera   3RD FLOOR COLLEGE BUILDING Room Time Subject / Section Professor
WEDNESDAY|3-1|9:30|12:00|History of Math & Adv. Alg.|BSE 5|Ms. L. Umipig
WEDNESDAY|3-4|9:30|12:00|Facilitating Learning|EDUC 3|Dr. Valdecanto
WEDNESDAY|3-6|9:30|12:00|Financial Mgt.|BSBA 3A|Mr. Pike
WEDNESDAY|3-8|9:30|12:00|Math in the Modern World|BSBA 1E|Mr. Figueroa
WEDNESDAY|3-9|7:30|9:30|The Contemporary World|HM 2A|Mr. Villalon
WEDNESDAY|3-9|9:30|12:00|Supply Chain|BSHM 2E|Dr. Gabriel
WEDNESDAY|3-9|1:30|4:00|Gastro Op.|BSHM 3M|Ms. Castillo
WEDNESDAY|3-10|7:30|9:30|Food & Bev.|BSHM 2E|Mr. Millanes
WEDNESDAY|3-10|9:30|12:00|Foreign Lang.|BSHM 2A|Mr. Millanes
WEDNESDAY|3-10|1:30|4:00|PE3|BSHM 1M|Mr. Valdez
WEDNESDAY|3-11|7:30|9:30|Phil. Culture|BSHM 2A|Mr. Retiro
WEDNESDAY|3-11|9:30|12:00|OLLC Ethics|BSHM 1M|Mr. Cortez
WEDNESDAY|3-11|1:30|4:00|Purposive Comm.|BSHM 1D|Mrs. Rosarino
WEDNESDAY|3-12|7:30|9:30|PE3|BSHM 2D|Ms. Hadloc
WEDNESDAY|3-12|9:30|12:00|PE1|BSHM 1M|Ms. Hadloc
WEDNESDAY|3-12|4:00|7:30|Readings in Phil. History|BSHM 1M|Mrs. Zipagan
WEDNESDAY|3-1|4:00|7:30|The Contemporary World|EDUC 1|Dr. Urmaza
WEDNESDAY|3-2|4:00|7:30|Speech & Stage Arts|BSE 2|Mr. San Miguel
WEDNESDAY|3-3|4:00|7:30|Tech. Writing|BSE 2|Mrs. San Miguel
WEDNESDAY|3-4|4:00|7:30|GMC|BSE 2|Mrs. Peralta
WEDNESDAY|3-5|1:30|4:00|PE3|BSIT 1M|Mr. Valdez
WEDNESDAY|3-7|9:30|12:00|Sining ng Kom.|BSBA 2M|Mrs. Pike   
THURSDAY|3-1|10:00|12:00|Teaching Math in Elem. Primary|BEED 1|Mr. Cortez
THURSDAY|3-1|1:30|4:00|Research in Educ|BEED 3|Dr. Dillena
THURSDAY|3-2|1:30|4:00|Teaching English in Elem.|BEED 1|Dr. Malabanan
THURSDAY|3-3|1:30|4:00|Logic & Set Theory|Math 2|Mr. Frondozo
THURSDAY|3-3|4:00|7:30|Pagtuturo ng FIl sa Elem.|BEED 2|Mrs. Peralta
THURSDAY|3-6|10:00|12:00|OLLC Ethics|BSBA 1B|Mrs. Umipig
THURSDAY|3-6|10:00|12:00|Basic Micro Econ|BSBA 1A|Mr. Pike
THURSDAY|3-6|1:30|4:00|Basic Micro Econ|BSBA 1B|Mr. Pike
THURSDAY|3-6|4:00|7:30|Great Books|BSBA 2B|Mrs. Umipig
THURSDAY|3-7|1:30|3:00|Sining ng Kom.|BSBA 2A|Mr. Cruz
THURSDAY|3-9|7:30|9:30|Applied Bus. Tools|BSHM 2E|Ms. Diamco
THURSDAY|3-9|9:30|12:00|Applied Bus. Tools|BSHM 2D|Ms. Diamco
THURSDAY|3-9|1:30|4:00|PE3|BSHM 2B|Mr. V. Santos
THURSDAY|3-9|4:00|7:30|Supply Chain|BSHM 2C|Dr. Gabriel
THURSDAY|3-10|7:30|9:30|Phil. Pop Culture|BSHM 4B|Mr. Millanes
THURSDAY|3-10|9:30|12:00|Supply Chain|BSHM 2E|Dr. Gabriel
THURSDAY|3-10|1:30|4:00|Supply Chain|BSHM 2D|Dr. Gabriel
THURSDAY|3-11|7:30|9:30|Food Styling|BSHM 2B|Mrs. V. Castillo
THURSDAY|3-11|9:30|12:00|Food Styling|BSHM 2C|Mrs. V. Castillo
THURSDAY|3-12|7:30|9:30|STS The Entrep.|BSHM 2A|Mr. Retiro
THURSDAY|3-12|1:30|4:00|Purposive Comm.|BSHM 2B|Dr. Urmaza
THURSDAY|3-12|4:00|7:30|Purposive Comm.|BSHM 2D|Dr. Urmaza
THURSDAY|4-1|7:30|9:30|The Contemporary World|BSHM 3C|Mr. Villalon
THURSDAY|4-1|7:30|9:30|PE3|BSIT 2D|Ms. Hadloc   4TH FLOOR COLLEGE BUILDING Room Time Subject / Section Professor
THURSDAY|4-2|7:30|9:30|Fund. of Martial Arts|BSC 1B|Mr. Alivar
THURSDAY|4-3|7:30|9:30|Fund. of Martial Arts|BSC 1A|Mr. Alivar
THURSDAY|4-4|9:30|12:00|Traffic Mgt. & Acci. Dent.|CRIM 3C|Mr. Tumalip
THURSDAY|4-4|1:30|4:00|The Contemporary World|BSC 1E|Dr. Malabanan
THURSDAY|4-5|9:30|12:00|Comparative Policing|CRIM 2E|Mr. Noceda
THURSDAY|4-5|1:30|4:00|Comparative Policing|CRIM 2C|Mr. Noceda
THURSDAY|4-5|4:00|7:30|Comparative Policing|CRIM 2D|Mr. Noceda
THURSDAY|4-6|7:30|10:00|Theories & Causation|CRIM 2A|Dr. Ramos
THURSDAY|4-6|1:30|4:00|Theories & Causation|CRIM 2D|Dr. Ramos
THURSDAY|4-6|4:00|7:30|Philippine Lit.|CRIM 2E|Ms. Donato
THURSDAY|4-7|9:30|12:00|First Aid & Water Surv.|BSC 2B|Mr. Legacion
THURSDAY|4-7|1:30|4:00|First Aid & Water Surv.|BSC 2A|Mr. Legacion
THURSDAY|4-8|7:30|10:00|Law Enforcement Org. & Admin.|CRIM 2C|Mr. Marquez
THURSDAY|4-8|1:30|4:00|Law Enforcement Org. & Admin.|CRIM 2D|Mr. Marquez
THURSDAY|4-8|4:00|7:30|Law Enforcement Org. & Admin.|CRIM 2A|Mr. Marquez  4-9 — — —
THURSDAY|4-11|7:30|9:30|IT Capstone 2|IT 4A|Mr. Sagun
THURSDAY|4-11|9:30|12:00|EVET Driven 2|IT 4A|Mr. Sagun
THURSDAY|4-11|1:30|3:00|Math in the Modern World|BSIT 1B|Mrs. Duldulao
THURSDAY|4-11|3:00|5:30|Understanding the Self|BSIT 1B|Mrs. Del Rosario
THURSDAY|4-11|5:30|7:30|EVET Driven 2|IT 4B|Mr. Sagun
THURSDAY|4-12|1:30|3:00|IT Capstone 2|IT 4B|Mr. Sagun
THURSDAY|4-12|3:00|5:30|The Contemporary World|BSIT 1B|Mrs. Zipagan
THURSDAY|4-13|1:30|3:00|Math in the Modern World|BSIT 1C|Mr. Figueroa
THURSDAY|4-14|1:30|3:00|Comp. Prog 2|BSIT 2B|Mr. Acebuche
THURSDAY|4-14|5:30|7:30|IT Tech Writing|BSIT 3A|Ms. San Miguel
THURSDAY|4-2|4:00|7:30|Understanding the Self|CRIM 1B|Mr. Degula
THURSDAY|4-3|4:00|7:30|Rizal|CRIM 2B|Dr. Jaf Reyes   
FRIDAY|3-1|7:30|9:30|Teaching in Multigrade|BEED 2|Mr. Villalon
FRIDAY|3-1|4:00|7:30|NSTP 1|BEED 1|Mr. Zipagan
FRIDAY|3-2|4:00|7:30|Language & Culture & Society|BEED 1|Mr. San Miguel
FRIDAY|3-4|4:00|7:30|Tech for Teaching & Learning|BEED 3|Mr. A. Sibayano
FRIDAY|3-5|7:30|9:30|Readings in Phil. History|BSIT 1C|Dr. Jaf Reyes
FRIDAY|3-5|9:30|12:00|Readings in Phil. History|BSHM 1E|Dr. Jaf Reyes
FRIDAY|3-5|1:30|4:00|Readings in Phil. History|BSIT 1C|Dr. Jaf Reyes
FRIDAY|3-5|4:00|7:30|Readings in Phil. History|BSC 1C|Dr. Jaf Reyes
FRIDAY|3-6|9:30|12:00|Fin. Mgt.|BSBA 3A|Mr. Pike
FRIDAY|3-6|1:30|4:00|Purposive Comm.|BSBA 1A / 1B|Ms. Ronsable
FRIDAY|3-6|4:00|7:30|Math in the Modern World — Purposive Comm.|BSBA 1A|Mrs. Umipig
FRIDAY|3-8|9:30|12:00|Food & Bev. Op.|BSHM 1A / 1M|Ms. Catbagan
FRIDAY|3-8|1:30|4:00|Food & Bev. Op.|BSHM 1A / 1M|Ms. Catbagan
FRIDAY|3-9|7:30|9:30|Strategic Mgt.|BSHM 3B|Dr. Gabriel
FRIDAY|3-9|9:30|12:00|Research in Hospitality|BSHM 1E|Dr. Gabriel
FRIDAY|3-9|1:30|4:00|Strategic Mgt.|BSHM 2M|Dr. Gabriel
FRIDAY|3-10|7:30|9:30|French Cuisine|BSHM 2E|Mr. Tubana
FRIDAY|3-10|9:30|12:00|STS The Entrep.|BSHM 1M|Mr. Retiro
FRIDAY|3-10|1:30|4:00|French Cuisine|BSHM 2M|Mr. Tubana
FRIDAY|3-11|9:30|12:00|OLLC Ethics|BSHM 1M|Mr. Cortez
FRIDAY|3-11|1:30|4:00|Purposive Comm.|BSHM 1M|Dr. Urmaza   4TH FLOOR COLLEGE BUILDING Room Time Subject / Section Professor
FRIDAY|4-2|7:30|9:30|Forensic Ballistics|CRIM 4B|Mr. Noceda
FRIDAY|4-2|9:30|12:00|Theories & Causation|CRIM 2D|Dr. Ramos
FRIDAY|4-2|1:30|4:00|Phil. Lit.|CRIM 2B|Ms. Tiongson
FRIDAY|4-2|4:00|7:30|Phil. Lit.|CRIM 2C|Ms. Donato
FRIDAY|4-3|1:30|4:00|Math in the Modern World|CRIM 1N|Mr. Frondozo
FRIDAY|4-3|4:00|7:30|Sining ng Kom.|CRIM 1K|Mr. Pespas
FRIDAY|4-4|7:30|9:30|Forensic Ballistics|CRIM 4A|Mr. Noceda
FRIDAY|4-4|9:30|12:00|Forensic Ballistics|CRIM 4C|Mr. Noceda
FRIDAY|4-4|1:30|4:00|Readings in Phil. History|CRIM 1A|Mr. Malabanan
FRIDAY|4-4|4:00|7:30|Readings in Phil. History|CRIM 1H|Mr. Malabanan
FRIDAY|4-5|7:30|9:30|Vice & Drug.|CRIM 4A|Mr. Pahitong
FRIDAY|4-5|9:30|12:00|Vice & Drug.|CRIM 4C|Mr. Pahitong
FRIDAY|4-5|1:30|4:00|Forensic Drug.|CRIM 4D|Mr. Pahitong
FRIDAY|4-5|4:00|7:30|Forensic Drug.|CRIM 4E|Mr. Pahitong
FRIDAY|4-6|7:30|9:30|Prof. Conduct & Ethics|CRIM 3J|Mr. Tungsol
FRIDAY|4-6|9:30|12:00|Prof. Conduct & Ethics|CRIM 3A|Mr. Tungsol
FRIDAY|4-6|1:30|4:00|Prof. Conduct & Ethics|CRIM 3D|Mr. Tungsol
FRIDAY|4-7|4:00|7:30|Crim Law 1|CRIM 3E|Atty. Tepase
FRIDAY|4-8|1:30|4:00|Char. Formation & Leadership|BSIT 2D|Dr. Dillena
FRIDAY|4-9|4:00|7:30|Specialized Crime|CRIM 3D|Mr. De Leon
FRIDAY|4-10|7:30|9:30|Math & Science Tech|BSIT 2A|Mr. Millanes
FRIDAY|4-10|4:00|7:30|Understanding the Self|BSIT 1D|Mr. Degula
FRIDAY|4-11|9:30|12:00|E-Commerce|BSIT 1B|Mr. Sagun
FRIDAY|4-11|1:30|4:00|Event Driven 1|BSIT 3A|Mr. Sagun
FRIDAY|4-11|4:00|7:30|Event Driven 1|BSIT 1B|Mr. Sagun
FRIDAY|4-12|1:30|4:00|Integrative Prog 2|BSIT 1A|Mr. Acebuche
FRIDAY|4-12|4:00|7:30|Integrative Prog 2|BSIT 1B|Mr. Acebuche
FRIDAY|4-13|1:30|4:00|NSTP 1|BSIT 1A|Mr. Cabrera
FRIDAY|4-13|4:00|7:30|Understanding the Self|BSIT 1A|Mrs. Degula  
SATURDAY|3-1|7:00|9:30|Teaching in Multigrade|BEED 2|Mr. Limbauan
SATURDAY|3-1|10:00|12:00|Structure of English|BEED 1|Mr. Umipig
SATURDAY|3-1|1:30|4:00|Survey of Lit. in Phil.|BEED 2|Ms. Agas
SATURDAY|3-2|7:00|9:30|LIterature|BEED 2|Ms. Dela Cruz
SATURDAY|3-2|10:00|12:00|Content & Ped. in Elem.|BEED 1|Mr. San Miguel
SATURDAY|3-3|7:00|9:30|The Contemporary World|BEED 1|Dr. Dillena
SATURDAY|3-3|10:00|12:00|Teaching Elem. Math|BEED 1|Dr. De Ola
SATURDAY|3-3|1:30|4:00|Inquiries, Investigations|BEED 3|Mr. Agas
SATURDAY|3-4|7:00|9:30|NSTP 1|BEED 2|Mr. Guinto
SATURDAY|3-4|1:30|4:00|Understanding the Self|BEED 2|Mr. Guinto
SATURDAY|3-5|7:00|9:30|NSTP 1|BSIT 1Q|Mr. Guinto
SATURDAY|3-5|10:00|12:00|Intro to Computing|BSIT 1Q|Mr. M. Sabinay
SATURDAY|3-5|1:30|4:00|Intro to Computing|BSIT 1Q|Mr. M. Sabinay
SATURDAY|3-5|4:00|7:30|Understanding the Self|BSIT 1Q|Mr. Duldulao
SATURDAY|3-6|7:00|9:30|Business Corresp.|BSBA 1A|Ms. Valencia
SATURDAY|3-6|10:00|12:00|Purposive Comm.|BSBA 1E|Ms. Valencia
SATURDAY|3-6|1:30|4:00|Logistics|BSBA 3A|Dr. Millanes
SATURDAY|3-7|7:00|9:30|Readings in Phil. Hist.|BSHM 1A|Ms. B. Ong
SATURDAY|3-7|10:00|12:00|PE|BSHM 1E|Mr. Figueroa
SATURDAY|3-7|1:30|4:00|The Contemp. World|BSHM 1A|Ms. B. Ong
SATURDAY|3-8|7:00|9:30|Personal Dev.|HM 1C|Ms. M. Panen
SATURDAY|3-8|10:00|12:00|Entrepreneurship|BSHM 1A|Mr. Millanes
SATURDAY|3-8|1:30|4:00|Rizal|BSHM 1A|Mr. Millanes
SATURDAY|3-8|4:00|7:30|Readings in Phil. Hist.|BSHM 1A|Ms. B. Ong
SATURDAY|3-9|7:00|9:30|NSTP 1|HM 1A|Mr. Tingson
SATURDAY|3-9|10:00|12:00|Rizal|BSHM 1A|Mr. Tingson
SATURDAY|3-10|7:00|9:30|Ethics|BSHM 1M|Mr. Pantina
SATURDAY|3-10|1:30|4:00|Ethics|BSHM 1M|Mr. Pantina
SATURDAY|3-11|7:00|9:30|ERGO|BSIT 1A|Mr. Bajo
SATURDAY|3-11|10:00|12:00|Prog. Fund.|BSIT 1A|Mr. Bajo
SATURDAY|3-11|1:30|4:00|ERGO & Prog.|BSIT 1A|Mr. Bajo
SATURDAY|4-1|7:00|9:30|ERGO|BSIT 1D|Mr. R. Llanera
SATURDAY|4-1|10:00|12:00|Prog. Fund.|BSIT 1D|Mr. R. Llanera
SATURDAY|4-1|1:30|4:00|ERGO & Prog.|BSIT 1D|Mr. R. Llanera
SATURDAY|4-2|7:00|9:30|Math in the Mod. World|BSIT 1A|Mr. Llanera
SATURDAY|4-2|10:00|12:00|Math in the Mod. World|BSIT 1A|Mr. Llanera
SATURDAY|4-2|1:30|4:00|Math in the Mod. World|BSIT 1A|Mr. Llanera
SATURDAY|4-3|7:00|9:30|Readings in Phil. Hist.|BSIT 1J|Ms. M. Llanera
SATURDAY|4-3|10:00|12:00|Readings in Phil. Hist.|BSIT 1J|Ms. M. Llanera
SATURDAY|4-3|1:30|4:00|Math in the Mod. World|BSIT 1J|Mr. Llanera
SATURDAY|4-4|7:00|9:30|NSTP 1|BSIT 1M|Mr. Bernardo
SATURDAY|4-4|10:00|12:00|NSTP 1|BSIT 1M|Mr. Bernardo
SATURDAY|4-4|1:30|4:00|Understanding the Self|BSIT 1M|Mr. Bernardo
SATURDAY|4-5|7:00|9:30|Human Behav. in Org.|BSIT 1M|Mr. Bernardo
SATURDAY|4-5|4:00|7:30|Human Behav. in Org.|BSIT 1M|Mr. Bernardo   FOURTH FLOOR COLLEGE BUILDING Room Time Subject / Section Professor
SATURDAY|4-7|7:00|9:30|Conduct & Discipline|CRIM 2M|Mr. Demetria
SATURDAY|4-7|10:00|12:00|Prof. Conduct & Ethics|CRIM 2M|Mr. Tunggol
SATURDAY|4-7|1:30|4:00|Criminal Law 1|CRIM 2M|Mr. Demetria
SATURDAY|4-7|4:00|7:30|Human Behav.|CRIM 2M|Mr. Demetria
SATURDAY|4-8|7:00|9:30|Human Behav.|CRIM 2K|Mr. 2K
SATURDAY|4-8|10:00|12:00|Criminal Law 1|CRIM 2K|Mr. Demetria
SATURDAY|4-8|1:30|4:00|Criminal Law 1|CRIM 2K|Mr. Demetria
SATURDAY|4-9|7:00|9:30|Special Crime Invest.|CRIM 2J|Mr. Reyes
SATURDAY|4-9|10:00|12:00|Special Crime Invest.|CRIM 2J|Mr. Reyes
SATURDAY|4-9|1:30|4:00|Rizal|CRIM 2J|Mr. Reyes
SATURDAY|4-9|4:00|7:30|Rizal|CRIM 2J|Mr. Reyes
SATURDAY|4-10|7:00|9:30|Ethics|BSIT 2A|Mr. Limbo
SATURDAY|4-10|10:00|12:00|Ethics|BSIT 2A|Mr. Limbo
SATURDAY|4-10|1:30|4:00|Data Comm.|BSIT 2A|Mr. Limbo
SATURDAY|4-11|7:00|9:30|Living in the IT Era|BSIT 2K|Mr. Millanes
SATURDAY|4-11|10:00|12:00|Sys. Admin.|BSIT 2K|Mr. Paunan
SATURDAY|4-11|1:30|4:00|Living in the IT Era|BSIT 2K|Mr. Millanes
SATURDAY|4-12|7:00|9:30|PE|BSIT 2B|Mr. Paunan
SATURDAY|4-12|10:00|12:00|PE|BSIT 2B|Mr. Paunan
SATURDAY|4-12|1:30|4:00|Emerging Tech.|BSIT 2B|Mr. Paunan
SATURDAY|4-13|7:00|9:30|PE|BSIT 2E|Mr. Paunan
SATURDAY|4-13|10:00|12:00|PE|BSIT 2E|Mr. Paunan
SATURDAY|4-13|1:30|4:00|Emerging Tech.|BSIT 2E|Mr. Paunan
SATURDAY|4-14|7:00|9:30|Readings in Phil. Hist.|BSIT 2M|Ms. M. Llanes
SATURDAY|4-14|10:00|12:00|Readings in Phil. Hist.|BSIT 2M|Ms. M. Llanes
SATURDAY|4-14|1:30|4:00|The Contemporary World|BSIT 2M|Ms. M. Llanes    HIGH SCHOOL BUILDING Room Time Subject / Section Professor
SATURDAY|201|7:00|9:30|Understanding the Self|BSIT 1M|Ms. Malmihan
SATURDAY|201|10:00|12:00|Understanding the Self|BSIT 1M|Ms. Malmihan
SATURDAY|201|1:30|4:00|Malmihan|BSIT 1M|Ms. Malmihan
SATURDAY|202|7:00|9:30|Non-Formal Ed.|CRIM 2L|Mr. Llorando
SATURDAY|202|10:00|12:00|Non-Formal Ed.|CRIM 2L|Mr. Llorando
SATURDAY|202|1:30|4:00|NSTP 2|CRIM 2L|Mr. Llorando
SATURDAY|203|7:00|9:30|JAY|CRIM 2M|Mr. Panting
SATURDAY|203|10:00|12:00|Char. & Leadership|CRIM 2M|Mr. Panting
SATURDAY|205|7:00|9:30|Res. Meth.|CRIM 2M|Mr. Panting
SATURDAY|205|10:00|12:00|Math in the Mod. World|CRIM 2M|Mr. Panting
SATURDAY|206|7:00|9:30|Res. Meth.|CRIM 2C|Mr. Hansiba
SATURDAY|206|10:00|12:00|Math in the Mod. World|CRIM 2C|Mr. Hansiba
SATURDAY|206|1:30|4:00|Math in the Mod. World|CRIM 2C|Mr. Hansiba
SATURDAY|207|7:00|9:30|REA & C|CRIM 2C|Mr. Hansiba
SATURDAY|207|10:00|12:00|REA & C|CRIM 2C|Mr. Hansiba
SATURDAY|209|7:00|9:30|Criminology|CRIM 2C|Mr. Cala
SATURDAY|209|10:00|12:00|Dispute Resolution|CRIM 2C|Mr. Cala
SATURDAY|209|1:30|4:00|ATI / IT|CRIM 2C|Mr. Cala
SATURDAY|210|7:00|9:30|Dispute Resolution|CRIM 2A|Mr. Cala
SATURDAY|210|10:00|12:00|Dispute Resolution|CRIM 2A|Mr. Cala
SATURDAY|210|1:30|4:00|Dispute Resolution|CRIM 2A|Mr. Cala
SATURDAY|210|4:00|7:30|ATI|CRIM 2A|Mr. Cala
""";

    public List<Schedule> findAll() {
        List<Schedule> schedules = new ArrayList<>();

        for (String line : DATA.split("\\R")) {
            String[] parts = line.split("\\|", -1);

            if (parts.length != 7) {
                continue;
            }

            schedules.add(new Schedule(
                parts[0],
                parts[1],
                normalizeScheduleStart(parts[2]),
                normalizeScheduleEnd(parts[2], parts[3]),
                parts[4],
                parts[5],
                parts[6]
            ));
        }

        return schedules;
    }

    private static String normalizeScheduleStart(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);

        if (hour >= 1 && hour <= 6) {
            hour += 12;
        }

        return String.format("%02d:%02d", hour, minute);
    }

    private static String normalizeScheduleEnd(String startText, String endText) {
        int startHour = Integer.parseInt(startText.split(":")[0]);
        int endHour = Integer.parseInt(endText.split(":")[0]);
        int endMinute = Integer.parseInt(endText.split(":")[1]);

        boolean afternoon = (startHour >= 1 && startHour <= 6) || startHour == 12;
        if (afternoon && endHour >= 1 && endHour <= 7) {
            endHour += 12;
        }

        return String.format("%02d:%02d", endHour, endMinute);
    }
}
