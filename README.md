# Plutarch Parallel Lives


## Description
The goal of Plutarch Parallel Lives is to visually demonstrate the story of the relations of a database schema, expressed as parallel lives on the screen.
The main modeling element is the Change Analysis per Relation and Transition matrix that has one row per table and one column per version of the schema history. Each cell reports on the state of the table at this particular version (non existent, idle, changed).
This state is also visualized at the front end, via the main visual output of the tool, the Parallel Lives Diagram (PLD).
As the detailed PLD becomes too long to observe with a single view, we provide clustering mechanisms to (a) reduce the number of rows, by clustering to groups of tables that have similar lives, and, (b) to reduce the number of columns, by producing phases of homogeneous internal activity, rater than individual versions, again by clustering versions.   


## Credits and History

### v.0.2 [2015 October]
The next version of Plutarch Parallel Lives was the MSc Thesis of Theofanis Giahos, completed in October 2015 (Univ. Ioannina, Dept. of CSE, Supervisor: P. Vassiliadis)
We enrich the model with table clusters and phases. The front end includes much more zooming and drilling and new versions of the PLD.

### v.0.1 [2013 May]
The first version of Plutarch Parallel Lives was the Diploma Thesis of Theofanis Giahos, completed in end May 2013 (Univ. Ioannina, Dept. of CS, Supervisor: P. Vassiliadis)
Front-end includes a simple PLD & some preliminary stats.