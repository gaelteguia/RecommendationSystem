# RecommendationSystem


1.	Contexte et objectifs du projet
Ce projet a été réalisé dans le cadre du cours de Master WEM (Web Mining). Il s’agit d’un outil de suggestions de ressources numériques en lien avec le domaine de l’éducation. Les ressources électroniques d’enseignement et d’apprentissage proposées proviennent de la banque de données bdper.plandetudes.ch fournie par la CIIP (Conférence Intercantonale de l'Instruction Publique).
Les enseignants, les élèves et leurs parents pourraient être les utilisateurs potentiels d’une telle plateforme.
Le système de recommandation se base sur des « keyphrases » extraits automatiquement grâce à l’outil de Topic Indexing Maui.

2.	Données 
La source des données est l’API d'accès BDPER(https://bdper.plandetudes.ch/api) qui expose près de 2000 ressources au format JSON. Les ressources contiennent chacune des métadonnées descriptives (titre, url, description, …) sur un fichier final au format PDF.

3.	Planification, répartition du travail
Etape 1 : Récupération des ressources
Etape 2 : Crawling des URLs et Indexation
Etape 3 : Keyphrases extraction
Etape 4 : Réalisation de l’interface utilisateur

4.	Fonctionnement
Un Client REST permet d’extraire les ressources depuis l’URL https://bdper.plandetudes.ch/api/v1/ressources/e-media/. Ensuite le crawler sert à parser les contenus des ressources finales et à les indexer à la volée. Les informations des fichiers PDF sont récupérées en écrites dans un fichier texte « contents.txt ». L’extraction de mots clés est effectuée sur le fichier texte. Et les « keyphrases » extraites sont proposées aux utilisateurs via une interface web, sous forme de checkboxes.
Le cas d’utilisation de l’application est la recherche de ressources en sélectionnant des cases proposées. Une recherche est faite sur l’index avec comme entrée les mots clés sélectionnés.

5.	Techniques, algorithmes et outils utilisés 
Pour le parsing de PDF j’ai utilisé Apache PDFBOX. Pour l’indexation de contenu j’ai utilisé l’API Apache Lucene version 7.  Pour l’extraction de « keyphrases » j’ai utilisé l’outil de Text mining Maui. Il m’a permis de réaliser :
-	La suppression de stopwords
-	La tokenisation/ stemming
-	La détermination de Termes-candidats
-	L’évaluation des résultats
Maui effectue une recherche de mots-clés en se basant sur les informations statistiques de la langue française. Si un mot apparaît plus de fois dans le texte que dans la langue anglaise en termes de probabilité, c'est un mot-clé candidat.
Pour la réalisation de l’interface avec cases à cocher j’ai utilisé le Framework JSF 2.0 et GlassFish 4 comme serveur web.

6.	Références
https://github.com/yasserg/crawler4j
https://github.com/NickQiZhu/maui-indexer

7.	 Conclusion
Ce projet m’a permis de mettre en œuvre des concepts vus en cours. Notamment le crawling, et l’utilisation de méthode de text mining pour effectuer de l’extraction de mots clés. Il ouvre des idées de prolongements, comme l’ajout de la notion de Rating pour recueillir les intérêts utilisateurs et ainsi effectuer des recommandations basées sur les préférences. 
