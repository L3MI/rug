
# Dans la table d'attributs carreaux, chaque ligne est composee de 16 attributs :
# 0 : ID
# 1 2 3 et 4 : coordonnees Xmin Xmax Ymin Ymax du carreau
# 5 : Nom de la commune
# 6 : Chef ou non
# 8 : population par commune pour 1962
# 9 : population 1968
# 10 : population 1975
# 11 : population 1982
# 12 : population 1990
# 13 : population 1999
# 14 : population 2010
# 15 : population extrapolee 2020

# Choisir l'annee en renseignant le numero de la colonne a diffuser
# Puis choisir le nom du fichier de sortie ligne 22
numero_colonne = 15

# Chargement des deux couches vectorielles
carreaux = QgsVectorLayer('C:/Users/doniasp/Documents/Projet Qgis/Carroyage 2010/200m-carreaux-RUG/carroyage_RUG_avant_diffusion.dbf', 'carreaux', 'ogr')
f = open('C:/Users/doniasp/Documents/Projet Qgis/Diffusion/test.txt', 'w')       # FICHIER DE SORTIE

from math import *
import copy

# declarations des variables, liste et tableau utilises
liste = []                               # liste qui contient les carreaux correspondants aux chefs-lieux des communes
resultat={}                          # tableau qui repertorie les valeurs affectees a chaque carreau par la diffusion
p = 1.2
seuil = 8


#                    DIFFUSION

# premier parcours de la table
for carreau in carreaux.getFeatures():
    attr = carreau.attributes()
    nomCommune = attr[5]
    nomChefLieu = attr[6]
    
    if (nomChefLieu != NULL):                      # correspond a un carreau chef-lieu
        z = copy.copy(attr)                     # on cree une copie a ajouter dans la liste
        liste.append(z)


# second parcours de la table
for carreau in carreaux.getFeatures():
    xi = carreau.attributes()
    
    sa = 0                         # variable pour la somme
    
    # parcours des 273 carreaux chefs-lieux
    for xk in liste:
        
        # si le carreau actuel n'est pas chef-lieu
        if xk[0] != xi[0]:
            dist = sqrt((xi[1] - xk[1])*(xi[1] - xk[1]) + (xi[3] - xk[3])*(xi[3] - xk[3]))
            dist = dist / 1000               # pour manipuler des distances en metres
            if dist > seuil:
                dist = 0
            
            d = pow(dist,p)
            
        # si le carreau actuel est chef lieu
        else:
            dist = 0.1*sqrt(2)
            d = pow(dist,p)
            
        # pour eviter les divisions par zero
        if (d > 0.00001):
                sa += xk[numero_colonne]/d

    # ajout de l'id et valeur calculee dans le tableau resultat
    id =xi[0]
    u = sa
    resultat[id]=u



#                    EQUILIBRAGE
# tableau qui contient la somme des valeurs associee aux carreaux par commune
list = {}

# initialisation des sommes a 0 par commune
for ligne in carreaux.getFeatures():
    attr = ligne.attributes()
    nomCommune = attr[5]
    list[nomCommune] = 0
    
# calcul de la somme des valeurs par commune
for ligne in carreaux.getFeatures():
    attr = ligne.attributes()
    id = attr[0]
    nomCommune = attr[5]
    list[nomCommune] += resultat[id]


# parcours de la table d'attributs pour realiser l'equilibrage des valeurs diffusees
for ligne in carreaux.getFeatures():
    attr = ligne.attributes()
    id = attr[0]
    nomCommune = attr[5]
    u =  attr[numero_colonne] * resultat[id] / list[nomCommune]
    f.write(str(id) + ';' + str(u) + '\n')
  
    
print 'termine'

f.close()         # fermeture du fichier sortie

