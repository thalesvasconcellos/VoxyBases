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
@ConfigFile("safe.yml")
@TranslateColors
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SafeValue implements ConfigurationInjectable {

    @Getter private static final SafeValue instance = new SafeValue();

    @ConfigField("Comandos_ajuda") private List<String> helpCommands;
    @ConfigField("Permissao_abrir") private String openPermission;

    @ConfigField("Jogador_inativo") private boolean inactivePlayer;
    @ConfigField("Tempo_inativo") private int inactiveDays;
    @ConfigField("Hackear_jogador_offline") private boolean hackOfflinePlayer;

    @ConfigField("Cofre_item.material") private String safeMaterial;
    @ConfigField("Cofre_item.nome") private String safeName;
    @ConfigField("Cofre_item.lore") private List<String> safeLore;

    @ConfigField("Item_hacker.material") private String hackMaterial;
    @ConfigField("Item_hacker.nome") private String hackName;
    @ConfigField("Item_hacker.lore") private List<String> hackLore;

    @ConfigField("Item_parar_hacker.material") private String stopMaterial;
    @ConfigField("Item_parar_hacker.nome") private String stopName;
    @ConfigField("Item_parar_hacker.lore") private List<String> stopLore;

    @ConfigField("Mensagem_offline") private String offlineMessage;
    @ConfigField("Mensagem_recolher_Cofre") private String recallSafeMessage;

    @ConfigField("Sistema_upgrade.Segurança.nivel_1.tempo_hackear") private int levelOneSecurityHackTime;
    @ConfigField("Sistema_upgrade.Segurança.nivel_2.tempo_hackear") private int levelTwoSecurityHackTime;
    @ConfigField("Sistema_upgrade.Segurança.nivel_3.tempo_hackear") private int levelThreeSecurityHackTime;
    @ConfigField("Sistema_upgrade.Segurança.nivel_4.tempo_hackear") private int levelFourSecurityHackTime;
    @ConfigField("Sistema_upgrade.Segurança.nivel_5.tempo_hackear") private int levelFiveSecurityHackTime;

    @ConfigField("Sistema_upgrade.Segurança.nivel_2.custo_upgrade") private double levelTwoSecurityHackPrice;
    @ConfigField("Sistema_upgrade.Segurança.nivel_3.custo_upgrade") private double levelThreeSecurityHackPrice;
    @ConfigField("Sistema_upgrade.Segurança.nivel_4.custo_upgrade") private double levelFourSecurityHackPrice;
    @ConfigField("Sistema_upgrade.Segurança.nivel_5.custo_upgrade") private double levelFiveSecurityHackPrice;

    @ConfigField("Sistema_upgrade.Armazens_cofre.nivel_2.custo_upgrade") private double levelTwoStorageHackPrice;
    @ConfigField("Sistema_upgrade.Armazens_cofre.nivel_3.custo_upgrade") private double levelThreeStorageHackPrice;
    @ConfigField("Sistema_upgrade.Armazens_cofre.nivel_4.custo_upgrade") private double levelFourStorageHackPrice;
    @ConfigField("Sistema_upgrade.Armazens_cofre.nivel_5.custo_upgrade") private double levelFiveStorageHackPrice;

    @ConfigField("Mensagem_recolher_Cofre_cheio") private String recallFullSafeMessage;
    @ConfigField("Cofres_Quantidade") private int safeQuantity;
    @ConfigField("Cofres_Raio") private int safeRadius;

    @ConfigField("Alerta_hacking.tempo_title") private int alertTime;
    @ConfigField("Alerta_hacking.som_hacking") private String alertSound;
    @ConfigField("Alerta_hacking.title") private List<String> alertTitle;

    @ConfigField("Holograma_Cofre_altura") private double safeHologramHeight;
    @ConfigField("Holograma_Cofre") private List<String> safeHologram;

    @ConfigField("Holograma_Cofre_Hackeando") private List<String> safeHackingHologram;
    @ConfigField("Holograma_Cofre_Hackeado") private List<String> safeHackedHologram;

    @ConfigField("Tempo_holograma") private int safeStopHologramTime;
    @ConfigField("Holograma_Cofre_parar_Hackeamento") private List<String> safeStopHologram;

    public static <T> T get(Function<SafeValue, T> function) {
        return function.apply(instance);
    }
}