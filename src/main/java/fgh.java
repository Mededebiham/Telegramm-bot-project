import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class fgh extends TelegramLongPollingBot  {
    /*
    Description: Cette méthode retourne le nom d'utilisateur du bot. Ce nom d'utilisateur est défini lors de la création
     du bot avec le BotFather sur Telegram.
Rôle: Identifie le bot sur la plateforme Telegram.
Implémentation: Vous devez remplacer la chaîne vide "" par le nom d'utilisateur réel de votre bot, par exemple "MyAwesomeBot".

     */
    @Override
    public String getBotUsername() {
        return "MyAwesomeBot";
    }
/*
Description: Cette méthode est appelée lorsque le bot est enregistré auprès de l'API Telegram.
Elle appelle la méthode onRegister de la superclasse.
Rôle: Permet d'exécuter du code personnalisé au moment de l'enregistrement du bot.
Implémentation: Vous pouvez ajouter du code pour effectuer des actions spécifiques lors de
 l'enregistrement du bot, comme initialiser des ressources.
 */
    @Override
    public void onRegister() {
        super.onRegister();
        // Code personnalisé, par exemple, initialiser une base de données.
        System.out.println("Bot enregistré avec succès !");
    }
    /*
        Description: Cette méthode retourne le token d'authentification de votre bot. Ce token est fourni par
        le BotFather lors de la création du bot et est utilisé pour authentifier votre bot auprès de l'API Telegram.

Rôle: Le token est une clé secrète qui permet à votre bot de communiquer avec les serveurs de Telegram de manière sécurisée.
Implémentation: Vous devez remplacer la chaîne vide "" par le token réel fourni par le BotFather.
Ce token est une chaîne de caractères unique, généralement au format
 123456789:ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.
     */
@Override
    public String getBotToken() {
        return "123456789:ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    }
/*
Description: Cette méthode est appelée chaque fois que le bot reçoit une mise à jour, comme un message ou une commande.
Rôle: Traiter chaque mise à jour individuellement.
Implémentation: Vous devez ajouter du code pour définir la réaction du bot aux mises à jour.
 Par exemple, répondre aux messages texte.
 */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Vous avez dit : " + messageText);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }
/*
Description: Cette méthode est appelée lorsque le bot reçoit plusieurs mises à jour en même temps.
 Elle appelle la méthode onUpdatesReceived de la superclasse.
Rôle: Traiter un lot de mises à jour.
Implémentation: Vous pouvez personnaliser cette méthode pour définir comment le bot traite des lots
 de mises à jour. Par défaut, elle appelle la superclasse pour gérer les mises à jour individuellement via onUpdateReceived.

 */
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
        // Custom processing of the updates list
        for (Update update : updates) {
            onUpdateReceived(update);  // Process each update individually
        }
    }
}
