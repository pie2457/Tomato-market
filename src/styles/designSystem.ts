const colors = {
  white: "#FFF",
  grey50: "#FAFAFA",
  grey100: "#F9F9F9CC",
  grey200: "#F5F5F5B2",
  grey300: "#B3B3B31F",
  grey400: "#EFEFF0",
  grey500: "#B3B3B363",
  grey600: "#00000033",
  grey700: "#3C3C435C",
  grey800: "#3C3C4399",
  grey900: "#3C3C43",
  black: "#000",
  purple: "#5856D6",
  yellow: "#FFCC00",
  red: "#FF3B30",
};

const font = {
  displayStrong20: "700 20px Noto Sans KR, sans-serif",
  displayStrong16: "700 16px Noto Sans KR, sans-serif",
  displayDefault16: "400 16px Noto Sans KR, sans-serif",
  displayDefault12: "400 12px Noto Sans KR, sans-serif",

  availableStrong16: "700 16px Noto Sans KR, sans-serif",
  availableStrong12: "700 12px Noto Sans KR, sans-serif",
  availableStrong10: "700 10px Noto Sans KR, sans-serif",
  availableDefault16: "400 16px Noto Sans KR, sans-serif",
  availableDefault12: "400 12px Noto Sans KR, sans-serif",

  enabledStrong16: "700 16px Noto Sans KR, sans-serif",
  enabledStrong10: "700 10px Noto Sans KR, sans-serif",
};

export default {
  color: {
    neutral: {
      text: colors.grey900,
      textWeak: colors.grey800,
      textStrong: colors.black,
      background: colors.white,
      backgroundWeak: colors.grey50,
      backgroundBold: colors.grey400,
      backgroundBlur: colors.grey100,
      border: colors.grey500,
      borderStrong: colors.grey700,
      overlay: colors.grey600,
    },
    accent: {
      text: colors.white,
      textWeak: colors.black,
      primary: colors.purple,
      secondary: colors.yellow,
    },
    system: {
      warning: colors.red,
      background: colors.white,
      backgroundWeak: colors.grey200,
    },
  },
  filter: {
    neutralTextWeak:
      "brightness(0) saturate(100%) invert(15%) sepia(2%) saturate(5010%) hue-rotate(202deg) brightness(93%) contrast(73%)",
    accentText:
      "invert(100%) sepia(97%) saturate(15%) hue-rotate(110deg) brightness(103%) contrast(102%)",
  },
  backdropFilter: {
    blur: "blur(8px)",
  },
  font,
};
