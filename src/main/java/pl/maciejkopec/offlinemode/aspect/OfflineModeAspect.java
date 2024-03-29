package pl.maciejkopec.offlinemode.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.service.ResponseCaptor;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class OfflineModeAspect {

  private final ResponseCaptor responseCaptor;

  @Around("@annotation(offlineMode)")
  public Object captureOfflineCall(
      final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode) throws Throwable {
    final var METHOD = "captureOfflineCall(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final var value = responseCaptor.capture(joinPoint, offlineMode);

    log.debug("Leaving {}", METHOD);
    return value;
  }
}
