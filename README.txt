Pour démarrer l'application:
	1/ Exécuter la classe "Client.java" n fois pour lancer n machines dans le réseau p2p
	2/ Introduire l'id pour chaque mahine
		*Chaque machine est identifié par "peer_id:port" à la place de "@IP:port" (vue que les peers s’éxecutent sur la meme machine « localhost »)
		*Chaque peerX a une répertoire "../Peerx/(text files)" simulant son filesystem
	3/ Introduire le fichier à chercher dans la machine jouant le role de client:
	4/ Attendre la réponse

Déroulement du processus :
*Chaque machine joue le role de client et serveur :
	Partie Client:
		"ClientApp.java" : lance la requete pour la recherche d'un fichier dans le réseau.
		Cette classe va : - lance le serveur "server.java" & lancer le thread "DownloadHandlerServ" qui est un serveur pret à reçevoir une réponse à tout moment.
	Partie Client a les classes suivantes:
		"server.java": instance serveur en attente d'une requete de recherche (démarré par la classe "ClientApp.java")
		En cas de réception d'un requete de recherche, "server.java" va lancer "RespHandlerClient" qui va chercher le fichier et puis elle ouvre une socket avec la machine Sender, la répondre et lui envoyer le fichier s'il existe :
		Si le fichier n'existe pas, la classe va lancer "ForwardClient.java" qui va forwarder la requete au peer suivant. 
			
		