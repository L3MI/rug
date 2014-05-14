
# Chargement des deux couches vecteurs
communes = QgsVectorLayer('C:/Users/doniasp/Documents/Projet Qgis/Communes/Communes RUG/RUG.dbf', 'communes', 'ogr')
carreaux = QgsVectorLayer('C:/Users/doniasp/Documents/Projet Qgis/Carroyage 2010/200m-carreaux-RUG/carroyage_RUG_vide.dbf', 'carreaux', 'ogr')


if not carreaux.isValid() or not communes.isValid():
  print "Layer failed to load!"


# Parcours de toutes les communes de la RUG
for commune in communes.getFeatures():
    attr_c = commune.attributes()
    
    i = 0
    # Parcours de touts les carreaux
    for carreau in carreaux.getFeatures():
        attr_p = carreau.attributes()
        x_min = attr_p[1] / 100; x_max = attr_p[2] / 100
        y_min = attr_p[3] / 100; y_max = attr_p[4] / 100
        # Si le carreau contient l'emplacement du chef lieu de la commune courante
        if attr_c[5] >= x_min and attr_c[5] <= x_max and attr_c[6] >= y_min and attr_c[6] <= y_max:
            # Ecriture de la communes dont le carreau est le chef lieu
            carreaux.dataProvider().changeAttributeValues( { i : { 6 : attr_c[3] } } )
            break;
        i += 1


