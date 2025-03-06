package com.vasconcellos.voxybases.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Banners {

    A(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.MS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    B(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.WHITE, BannerType.CBO),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.MS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    C(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.MS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    D(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.WHITE, BannerType.CBO),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    E(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.MS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    F(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.MS),
            new BannerStep(DyeColor.WHITE, BannerType.RS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    G(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.HH),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    H(
            DyeColor.BLACK,
            new BannerStep(DyeColor.WHITE, BannerType.TS),
            new BannerStep(DyeColor.WHITE, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    I(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.CS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    J(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.HH),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    K(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.DRS),
            new BannerStep(DyeColor.WHITE, BannerType.HH),
            new BannerStep(DyeColor.BLACK, BannerType.DLS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    L(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    M(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.TT),
            new BannerStep(DyeColor.WHITE, BannerType.TTS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    N(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.TT),
            new BannerStep(DyeColor.BLACK, BannerType.DRS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    O(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    P(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.HHB),
            new BannerStep(DyeColor.BLACK, BannerType.MS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    Q(
            DyeColor.BLACK,
            new BannerStep(DyeColor.WHITE, BannerType.MR),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.BR),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    R(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.HH),
            new BannerStep(DyeColor.WHITE, BannerType.CS),
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.DRS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    S(
            DyeColor.BLACK,
            new BannerStep(DyeColor.WHITE, BannerType.MR),
            new BannerStep(DyeColor.WHITE, BannerType.MS),
            new BannerStep(DyeColor.BLACK, BannerType.DRS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    T(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.CS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    U(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    V(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.DLS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.WHITE, BannerType.BT),
            new BannerStep(DyeColor.BLACK, BannerType.DLS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    X(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.CR),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    W(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.BT),
            new BannerStep(DyeColor.WHITE, BannerType.BTS),
            new BannerStep(DyeColor.BLACK, BannerType.LS),
            new BannerStep(DyeColor.BLACK, BannerType.RS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    Y(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.DRS),
            new BannerStep(DyeColor.WHITE, BannerType.HHB),
            new BannerStep(DyeColor.BLACK, BannerType.DLS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    ),
    Z(
            DyeColor.WHITE,
            new BannerStep(DyeColor.BLACK, BannerType.TS),
            new BannerStep(DyeColor.BLACK, BannerType.DLS),
            new BannerStep(DyeColor.BLACK, BannerType.BS),
            new BannerStep(DyeColor.WHITE, BannerType.BO)
    );

    @Getter
    private final DyeColor base;
    private final BannerStep[] steps;

    Banners(DyeColor base, BannerStep... steps) {
        this.base = base;
        this.steps = steps;
    }

    public List<Pattern> getPatterns() {
        return Arrays.stream(steps).map(step -> new Pattern(step.getColor(),
                step.getType().get())).collect(Collectors.toList());
    }

    public ItemStack build() {
        ItemStack banner = new ItemStack(Material.BANNER, 1);
        BannerMeta meta = (BannerMeta) banner.getItemMeta();

        meta.setBaseColor(base);
        meta.setPatterns(getPatterns());

        banner.setItemMeta(meta);

        return banner;
    }

    public static Banners getByChar(char c) {
        return Arrays.stream(values()).filter(value -> value.name().charAt(0) ==
                Character.toUpperCase(c)).findAny().orElse(null);
    }

    @RequiredArgsConstructor
    @Getter
    enum BannerType {
        BL(PatternType.SQUARE_BOTTOM_LEFT),
        BR(PatternType.SQUARE_BOTTOM_RIGHT),
        TL(PatternType.SQUARE_TOP_LEFT),
        TR(PatternType.SQUARE_TOP_RIGHT),
        BS(PatternType.STRIPE_BOTTOM),
        TS(PatternType.STRIPE_TOP),
        LS(PatternType.STRIPE_LEFT),
        RS(PatternType.STRIPE_RIGHT),
        CS(PatternType.STRIPE_CENTER),
        MS(PatternType.STRIPE_MIDDLE),
        DRS(PatternType.STRIPE_DOWNRIGHT),
        DLS(PatternType.STRIPE_DOWNLEFT),
        SS(PatternType.STRIPE_SMALL),
        CR(PatternType.CROSS),
        SC(PatternType.STRAIGHT_CROSS),
        BT(PatternType.TRIANGLE_BOTTOM),
        TT(PatternType.TRIANGLE_TOP),
        BTS(PatternType.TRIANGLES_BOTTOM),
        TTS(PatternType.TRIANGLES_TOP),
        LD(PatternType.DIAGONAL_LEFT),
        RD(PatternType.DIAGONAL_RIGHT),
        LUD(PatternType.DIAGONAL_LEFT_MIRROR),
        RUD(PatternType.DIAGONAL_RIGHT_MIRROR),
        MC(PatternType.CIRCLE_MIDDLE),
        MR(PatternType.RHOMBUS_MIDDLE),
        VH(PatternType.HALF_VERTICAL),
        HH(PatternType.HALF_HORIZONTAL),
        VHR(PatternType.HALF_VERTICAL_MIRROR),
        HHB(PatternType.HALF_HORIZONTAL_MIRROR),
        BO(PatternType.BORDER),
        CBO(PatternType.CURLY_BORDER),
        CRE(PatternType.CREEPER),
        GRA(PatternType.GRADIENT),
        GRU(PatternType.GRADIENT_UP),
        BRI(PatternType.BRICKS),
        SKU(PatternType.SKULL),
        FLO(PatternType.FLOWER),
        MOJ(PatternType.MOJANG);

        private final PatternType type;

        public PatternType get() {
            return type;
        }
    }

    @RequiredArgsConstructor
    @Getter
    static final class BannerStep {

        private final DyeColor color;
        private final BannerType type;
    }
}