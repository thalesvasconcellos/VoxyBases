package com.vasconcellos.voxybases.config;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@ConfigFile("base.yml")
@TranslateColors
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseValue implements ConfigurationInjectable {

    @Getter private static final BaseValue instance = new BaseValue();

    @ConfigField("Comandos_ajuda") private List<String> helpCommands;

    @ConfigField("Opcoes.Desfazer_clan") private boolean undoClan;
    @ConfigField("Opcoes.Roubar_Base") private boolean stealBase;
    @ConfigField("Opcoes.Precisa_Hackear") private boolean needToHack;

    @ConfigField("Portao.Item_hacker.material") private String gateHackMaterial;
    @ConfigField("Portao.Item_hacker.nome") private String gateHackName;
    @ConfigField("Portao.Item_hacker.lore") private List<String> gateHackLore;

    @ConfigField("Portao.Item_parar_hacker.material") private String gateStopMaterial;
    @ConfigField("Portao.Item_parar_hacker.nome") private String gateStopName;
    @ConfigField("Portao.Item_parar_hacker.lore") private List<String> gateStopLore;

    @ConfigField("Portao.Ativar_invadindo_sem_jogadores_on") private boolean gateInvadeWithoutOnlinePlayers;
    @ConfigField("Portao.Tempo_hackear_portao") private int gateHackTime; // Segundos
    @ConfigField("Portao.Tempo_fechar_portao") private int gateCloseTime; // Minutos

    @ConfigField("Portao.Mensagem_ja_invadindo") private String gateAlreadyInvadingMessage;
    @ConfigField("Portao.Mensagem_invadindo_sem_jogadores_on") private String gateCantInvadeMessage;
    @ConfigField("Portao.ActionBar_hackeando_portao_segundos") private String gateActionHacking;
    @ConfigField("Portao.ActionBar_portao_hackeado") private String gateActionHacked;

    @ConfigField("Portao.Som_fechando") private String gateClosingSound;
    @ConfigField("Portao.Som_abrindo") private String gateOpeningSound;

    @ConfigField("Portao.Portao_fechando_title") private List<String> gateClosingTitle;
    @ConfigField("Portao.Portao_abrindo_title") private List<String> gateOpeningTitle;

    @ConfigField("Centro_de_controle.Item_hacker.material") private String safeHackMaterial;
    @ConfigField("Centro_de_controle.Item_hacker.nome") private String safeHackName;
    @ConfigField("Centro_de_controle.Item_hacker.lore") private List<String> safeHackLore;

    @ConfigField("Centro_de_controle.Item_parar_hacker.material") private String safeStopMaterial;
    @ConfigField("Centro_de_controle.Item_parar_hacker.nome") private String safeStopName;
    @ConfigField("Centro_de_controle.Item_parar_hacker.lore") private List<String> safeStopLore;

    @ConfigField("Centro_de_controle.Cofre_item") private String safeItem;
    @ConfigField("Centro_de_controle.Mensagem_erro_colocar_centro") private String safeErrorCentralMessage;
    @ConfigField("Centro_de_controle.Mensagem_possui_base") private String safeAlreadyHaveMessage;

    @ConfigField("Centro_de_controle.Aliados") private boolean safeAllies;
    @ConfigField("Centro_de_controle.Dropar_pegar") private boolean dropAndPickup;
    @ConfigField("Centro_de_controle.Lider_upgrades") private boolean safeOnlyLeaderCanUpgrade;

    @ConfigField("Centro_de_controle.Tempo_Parar_Invasão") private int timeToStopInvasion;

    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_1.tempo_hackear") private int levelOneSecurityHackTime;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_2.tempo_hackear") private int levelTwoSecurityHackTime;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_3.tempo_hackear") private int levelThreeSecurityHackTime;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_4.tempo_hackear") private int levelFourSecurityHackTime;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_5.tempo_hackear") private int levelFiveSecurityHackTime;

    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_2.custo_upgrade") private double levelTwoSecurityHackPrice;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_3.custo_upgrade") private double levelThreeSecurityHackPrice;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_4.custo_upgrade") private double levelFourSecurityHackPrice;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Segurança.nivel_5.custo_upgrade") private double levelFiveSecurityHackPrice;

    @ConfigField("Centro_de_controle.Sistema_upgrade.Armazens_cofre.nivel_2.custo_upgrade") private double levelTwoStorageHackPrice;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Armazens_cofre.nivel_3.custo_upgrade") private double levelThreeStorageHackPrice;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Armazens_cofre.nivel_4.custo_upgrade") private double levelFourStorageHackPrice;
    @ConfigField("Centro_de_controle.Sistema_upgrade.Armazens_cofre.nivel_5.custo_upgrade") private double levelFiveStorageHackPrice;

    @ConfigField("Holograma_Cofre_altura") private double safeHologramHeight;
    @ConfigField("Holograma_Cofre") private List<String> safeHologram;

    @ConfigField("Holograma_Cofre_Sem_clan") private List<String> safeNoClanHologram;

    @ConfigField("Holograma_Cofre_Hackeando") private List<String> safeHackingHologram;
    @ConfigField("Holograma_Cofre_Hackeado") private List<String> safeHackedHologram;
    @ConfigField("Holograma_Cofre_Tomado") private List<String> safeTakedHologram;

    @ConfigField("Tempo_holograma") private int safeStopHologramTime;
    @ConfigField("Holograma_Cofre_parar_Hackeamento") private List<String> safeStopHologram;

    @ConfigField("Alarm_Base.Som_Alarme") private boolean alarmSound;

    @ConfigField("Alarm_Base.ActionBar.Delay") private int alarmActionDelay;
    @ConfigField("Alarm_Base.ActionBar.Vitimas.Clan.Actionbar_1") private String alarmActionClan01;
    @ConfigField("Alarm_Base.ActionBar.Vitimas.Clan.Actionbar_2") private String alarmActionClan02;
    @ConfigField("Alarm_Base.ActionBar.Vitimas.Jogador.Actionbar_1") private String alarmActionPlayer01;
    @ConfigField("Alarm_Base.ActionBar.Vitimas.Jogador.Actionbar_2") private String alarmActionPlayer02;
    @ConfigField("Alarm_Base.ActionBar.Atacantes.Actionbar_1") private String alarmActionAttacking01;
    @ConfigField("Alarm_Base.ActionBar.Atacantes.Actionbar_2") private String alarmActionAttacking02;

    @ConfigField("Alarm_Base.Title.Delay") private int alarmTitleDelay;
    @ConfigField("Alarm_Base.Title.Vitimas.Clan.title") private List<String> alarmTitleClan;
    @ConfigField("Alarm_Base.Title.Vitimas.Jogador.title") private List<String> alarmTitlePlayer;
    @ConfigField("Alarm_Base.Title.Atacantes.title") private List<String> alarmTitleAttacking;

    public static <T> T get(Function<BaseValue, T> function) {
        return function.apply(instance);
    }
}