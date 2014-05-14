
from math import *

carreaux = QgsVectorLayer('C:\Users\Remi\Desktop\Remi\Fac\Projet\QGIS\Diffusion\Hemi_Final.dbf', 'hemi', 'ogr')
f = open('C:/Users/Remi/Desktop/Remi/Fac/Projet/QGIS/Carroyage_V1_2010/Carroyage_Commune2/fichier_to_xls.txt','w')

# Utilisation d'un hash qui a chaque nom de commune associe la distance totale de tout les carreaux de la commune, la distance
# du carreau le plus eloigne par rapport au chef-lieu et le nombre de carreau present dans la commune
hash = {}

# Initialisation du Hash
for carreau in carroyage.getFeatures():
    attr = carreau.attributes()
    nomChefLieu = attr[6]
    # Si le carreau est un chef lieu
    if (nomChefLieu != NULL):
        hash[nomChefLieu] = [0, 0, 0]


# Parcours de tout les carreaux de la RUG
for carreau in carroyage.getFeatures():
    attr = carreau.attributes()
    
    nomCommune = attr[5]
    x_carreau = attr[1] / 100.0
    y_carreau = attr[3] / 100.0
    x_chef_lieu = attr[7]
    y_chef_lieu = attr[8]
    
    dist = sqrt((x_chef_lieu - x_carreau)*(x_chef_lieu - x_carreau) + (y_chef_lieu - y_carreau)*(y_chef_lieu - y_carreau))
    
    # Distance totale des carreaux par rapport au chef-lieu de chaque commune
    hash[nomCommune][0] += dist
    # Distance du carreau le plus eloigne par rapport au chef-lieu
    hash[nomCommune][1] = max(hash[nomCommune][1], dist)
    # Nombre de carreaux présents dans la commune
    hash[nomCommune][2] += 1


# Parcours de tout les carreaux de la RUG
for carreau in carroyage.getFeatures():
    attr = carreau.attributes()
    
    nomCommune = attr[5]
    dTotal = hash[nomCommune][0]
    dMax = hash[nomCommune][1]
    n = hash[nomCommune][2]
    populationCommune = attr[9] * 1000
    
    x_carreau = attr[1] / 100.0
    y_carreau = attr[3] / 100.0
    x_chef_lieu = attr[7]
    y_chef_lieu = attr[8]
    
    dist = sqrt((x_chef_lieu - x_carreau)*(x_chef_lieu - x_carreau) + (y_chef_lieu - y_carreau)*(y_chef_lieu - y_carreau))
    id = attr[0]
    # Calcul de la population affecter au carreau
    u = populationCommune * ((dMax - dist) / ((dMax*n) - dTotal))
    # Ecriture dans le fichier de l'id du carreau avec la population calculee sur ce carreau
    f.write(str(id) + ';' + str(u) + '\n')

f.close()
print 'termine'


