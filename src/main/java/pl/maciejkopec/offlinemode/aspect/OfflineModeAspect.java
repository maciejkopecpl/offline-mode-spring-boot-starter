package pl.maciejkopec.offlinemode.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.config.OfflineModeConfiguration;
import pl.maciejkopec.offlinemode.service.ResponseCaptor;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class OfflineModeAspect {

  private final ResponseCaptor responseCaptor;
  private final OfflineModeConfiguration configuration;

  @Around("@annotation(offlineMode)")
  public Object captureOfflineCall(
      final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode) throws Throwable {
    final String METHOD = "captureOfflineCall(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final Object value =
        configuration.isEnabled()
            ? responseCaptor.capture(joinPoint, offlineMode)
            : joinPoint.proceed();

    log.debug("Leaving {}", METHOD);
    return value;
  }
}
